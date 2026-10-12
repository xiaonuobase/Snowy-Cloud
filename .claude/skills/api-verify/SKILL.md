---
name: api-verify
description: AI 接口自测闭环：SM2 加密登录拿 token → curl 直调新接口 → 断言 CommonResult → 出报告。让 AI 不依赖人工点页面就能验证自己生成的接口。触发场景：1) 刚写完 Controller/Service 要验证接口通不通 2) 用户报"接口不对"需要复现请求 3) 验证权限/参数校验行为 4) 需要 token 调 /doc.html 之外的接口。触发词：接口测试、自测、curl、token、登录拿token、调接口、验证接口、冒烟、smoke、401复现、请求复现、网关、/api/webapp、/api/bizapp、跨服务。注意：环境没起来先看 env-setup；报错排查的完整决策树见 bug-detective。
---

# AI 接口自测闭环

**核心价值**：后端在跑（Nacos 8848、web-app 9101、biz-app 9102、网关 9003）时，AI 可以自己完成"登录 → 拿 token → 调接口 → 断言结果"的完整验证，不必让用户手点页面。所有请求默认走网关 `http://localhost:9003`，按 URL 前缀转发：`/api/webapp/**` → snowy-web-app（auth/sys/dev/gen/client/mobile），`/api/bizapp/**` → snowy-biz-app（biz），网关会去掉前两段再转发。

## 第 1 步：确认后端活着

```bash
curl -s http://localhost:9101/          # web-app 直连，应输出 WELCOME
curl -s http://localhost:9102/          # biz-app 直连，应输出 WELCOME
curl -s http://localhost:9003/api/webapp/dev/config/sysBaseList   # 经网关的免登录接口，应返回 code 200
# 任一无响应 → 先走 env-setup（看 Nacos 是否起、服务是否注册）
```

## 第 2 步：生成 SM2 登录密文（后端强制 SM2，明文会 PWD_DECRYPT_ERROR）

登录口令必须先用**出厂公钥**做 SM2 加密（公钥硬编码在 `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonCryptogramUtil.java` 的 `PUBLIC_KEY`，前端 `snowy-admin-web/src/utils/smCrypto.js` 是同一把）。在**仓库根目录**用一条 node 命令产出：

```bash
# 密文是随机的，每次生成都不同——正常
node -e "const s=require('./snowy-admin-web/node_modules/sm-crypto');console.log(s.sm2.doEncrypt('Snowy@2026!','04298364ec840088475eae92a591e01284d1abefcda348b47eb324bb521bb03b0b2a5bc393f6b71dabb8f15c99a0050818b56b23f31743b93df9cf8948f15ddb54',1))"
```

依赖：`snowy-admin-web/node_modules/sm-crypto`（前端执行过 npm install 即有；没有则先装，或临时 `npm i sm-crypto --registry=https://registry.npmmirror.com` 到任意目录）。require 路径必须带 `./`（不带会被 node 当成包名解析而失败）。cipherMode=1（C1C3C2），与前端 smCrypto.js 一致。

## 第 3 步：登录拿 token

```bash
# 登录在 web-app 的 auth 插件，经网关走 /api/webapp 前缀
RESP=$(curl -s -X POST http://localhost:9003/api/webapp/auth/b/doLogin \
  -H "Content-Type: application/json" \
  -d '{"account":"superAdmin","password":"<上一步的SM2密文>","device":"PC"}')   # device 只接受 PC / APP / MINI，省略时默认 PC
echo "$RESP"
# 成功：{"code":200,"data":"<token字符串>",...}（doLogin 返回 CommonResult<String>，data 就是 token）
# 账密错：code!=200 且 msg 提示账号或密码错误（核对 Snowy@2026! 与密文重生成）
```

取 token：`TOKEN=$(echo "$RESP" | node -e "let d='';process.stdin.on('data',c=>d+=c).on('end',()=>{const j=JSON.parse(d);console.log(j.data||'')})")`

> 若系统开启了图形验证码（DEV_CONFIG 可配），doLogin 还需 validCode/validCodeReqNo——此时改用网关聚合文档 `http://localhost:9003/doc.html`（basic 认证 snowyAdmin/Snowy@2026!）手动调试，或让用户从浏览器复制 token（请求头名就是 `token`）。
> 登录会话写在 Redis（database 1），网关与 biz-app 都从同一个 Redis 读会话，所以一个 token 可同时调 /api/webapp 和 /api/bizapp 下的接口。

## 第 4 步：调目标接口并断言

```bash
# biz 业务（snowy-biz-app）走 /api/bizapp 前缀
curl -s "http://localhost:9003/api/bizapp/biz/xxx/page?current=1&size=10" -H "token: $TOKEN"
# 或 POST：
curl -s -X POST http://localhost:9003/api/bizapp/biz/xxx/add -H "token: $TOKEN" -H "Content-Type: application/json" -d '{...}'

# 平台插件（sys/dev/gen/auth/client/mobile，在 snowy-web-app）走 /api/webapp 前缀
curl -s "http://localhost:9003/api/webapp/sys/user/page?current=1&size=10" -H "token: $TOKEN"

# 定位"是网关问题还是服务问题"：绕过网关直连服务（不带前缀），同样带 token
curl -s "http://localhost:9102/biz/xxx/page?current=1&size=10" -H "token: $TOKEN"
```

**断言表**：

| 期望 | 判定 |
|---|---|
| 业务正常 | `code === 200`，`data` 结构正确（分页是 `data.records/total`） |
| 未登录 | 401/`code!==200` 且提示登录 → token 头没带或过期（头名是 `token`，不是 Authorization）；免登录接口仍 401 → 白名单只配了一处（网关 GatewayConfigure 与服务 GlobalConfigure 要同时配） |
| 无权限 | 403 类提示 → 角色未授权该接口 URL（superAdmin 也不例外：接口权限全部来自角色授权，代码里没有超管绕过；手写的新接口要先在角色管理给超管角色"授权权限"） |
| 500 且是超管专属接口 | ⚠️ 现状缺陷：非超管经网关访问超管路径时，网关 NotRoleException 分支在响应式环境取 Servlet 请求出错，返回 500 而不是 403——按"无权限"理解 |
| 404（经网关） | URL 前缀没加或写错（biz 要 /api/bizapp，平台插件要 /api/webapp）；新业务前缀未在网关路由登记 |
| 503 / Unable to find instance | 目标服务没注册到 Nacos（服务未启动或命名空间/分组不一致） |
| 参数校验失败 | msg 为 Param 里写的中文校验消息 |
| 业务异常 | msg 为 CommonException 抛出的中文消息——**这就是定位线索** |

## 第 5 步：输出验证报告

```markdown
## 接口自测报告
| 接口 | 结果 | 说明 |
|---|---|---|
| POST /api/webapp/auth/b/doLogin | ✅ 200 | token 获取成功 |
| GET /api/bizapp/biz/xxx/page | ✅ 200 | 返回 3 条记录，含逻辑删除过滤 |
| POST /api/bizapp/biz/xxx/add | ✅ 200 | 落库 ID=xxx（已查库核对） |
| POST /api/bizapp/biz/xxx/add（缺name） | ✅ 按预期拦截 | "name不能为空" |
```

有条件时用 mysql 复核落库（业务库 `snowy-cloud`；连接信息从 Nacos 配置 `snowy-biz-app-dev.yaml` 的数据源段解析）：
```bash
mysql -uroot -p*** snowy-cloud -e "SELECT ID,NAME,DELETE_FLAG FROM BIZ_XXX ORDER BY CREATE_TIME DESC LIMIT 3;"
```

## 使用原则

- 生成接口代码后**主动自测**再交付（本技能 = /dev 收尾步骤的自动化版）
- 测试数据用"测试-"前缀，验证完清理（或逻辑删除）
- 密文/口令不写入任何文件，只在命令行内联使用
- 自测发现问题 → 修复 → 重测，闭环后再报告
- 改了 biz 代码要重启 snowy-biz-app、改了平台插件要重启 snowy-web-app；改了白名单要重启对应服务**和网关**

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/controller/AuthController.java` | /auth/b/doLogin 定义（运行在 web-app） |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/service/impl/AuthServiceImpl.java` | SM2 解密 + SM3 比对、device 校验 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/enums/AuthDeviceTypeEnum.java` | device 取值 PC / APP / MINI |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonCryptogramUtil.java` | SM2 公钥与加解密 |
| `snowy-admin-web/src/utils/smCrypto.js` | 前端同款加密（cipherMode=1） |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关鉴权与免登录名单（带 /api/webapp、/api/bizapp 前缀） |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-gateway-app-dev.yaml` | 网关路由（StripPrefix=2）、Knife4j 聚合与 basic 认证 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml` | sa-token.token-name=token、数据源、Redis |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端 URL 前缀 → 网关前缀映射 |
