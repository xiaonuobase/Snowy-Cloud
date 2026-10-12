---
name: bug-detective
description: Snowy 问题排查方法论与高发故障库：启动失败、401/403、接口报错、数据查不到、前后端联调问题的诊断决策树。触发场景：1) 任何报错/不工作/异常排查 2) 启动失败或页面白屏 3) 权限/数据问题定位。触发词：Bug、报错、异常、错误、不工作、失败、排查、调试、401、403、404、500、503、白屏、启动失败、查不到、排查思路、Nacos、网关、服务注册、Feign、跨服务、NoSuchMethodError、串会话。
---

# Snowy 问题排查指南

## 诊断决策树（先分类再深入）

```
问题发生在哪一层？
├─ 后端启动失败 ──→ 树 A
├─ 接口报错（4xx/5xx/业务码） ──→ 树 B
├─ 数据不对（查不到/查错/删不掉） ──→ 树 C
├─ 前端异常（白屏/按钮不见/数据不刷） ──→ 树 D
└─ 网关/服务注册/跨服务调用（404/503/Feign/串会话） ──→ 树 E
```

先切层：**直连服务端口（9101/9102，不带 /api 前缀）正常、经网关（9003）不正常 → 网关层问题（树 E）**；直连也不正常 → 服务自身问题（树 A–C）。

## 树 A：后端启动失败

1. 端口被占？（8848 Nacos / 9003 网关 / 9101 web-app / 9102 biz-app；命令见 env-setup 自检）
2. Nacos 没起 / 配置没拉到 → 各服务本地 `application.yml` 只接 Nacos（`optional:nacos:` 导入），拉不到配置时启动**不会因此中止**，常见表现是端口变成默认 8080、报找不到数据源/Redis 连接参数（推导的典型症状，以实际日志为准）。核对：Nacos 已启动；命名空间 `snowy-cloud-dev`、分组 `dev`、data-id `snowy-web-app-dev.yaml` / `snowy-biz-app-dev.yaml` / `snowy-gateway-app-dev.yaml` 在 Nacos 控制台存在且启动 profile 是 dev
3. MySQL 连不上/库没导 → Nacos 库 `snowy-nacos-app-open`（nacos-db.sql 等）与业务库 `snowy-cloud`（`snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql`）都要导；数据源口令改 Nacos 配置，不在本地文件
4. Redis 连不上 → 本地 6379 是否启动（database 1）
5. JDK 版本 → 主栈用 17
6. 依赖没编译 → 根目录 `mvn clean install -DskipTests`
7. **NoSuchMethodError / NoClassDefFoundError / ClassNotFoundException（启动即挂）** → 常见原因是依赖版本冲突：根 pom 的 dependencyManagement 手工锁定了 logback、jackson 等版本，升级 Spring Boot 或改这些锁定值时，若与 Spring Boot BOM 期望的版本不一致就会出现；nacos/sentinel/xxl-job 三个中间件应用另有自己的 Spring Boot 2.7.18 + logback 1.2.13，若被主栈版本串用也会挂。用 `mvn dependency:tree -Dincludes=ch.qos.logback,com.fasterxml.jackson.core -pl <模块> -am` 看实际生效版本
8. **Bean 冲突 / 缺 Bean** → 同一应用里某插件的 `-func` 与 `-api-context` 不能同装（Bean 名冲突，如 loginUserApi）；biz-app 注入了没有 ContextBean 的 Api（dev 侧 Dict/Log/Job/Push/WeakPassword、sys 侧 Menu/Button/Module 目前没有）会缺 Bean（推导，使用前请先确认）
9. Knife4j 401 → 网关聚合文档 `http://localhost:9003/doc.html` 的 basic 认证是 snowyAdmin/Snowy@2026!

## 树 B：接口报错

| 现象 | 根因 | 处理 |
|---|---|---|
| 401 未登录 | token 没带/过期；或接口不在白名单也确实需要登录 | 检查请求头 `token`；新公开接口要**同时**加网关 `GatewayConfigure.NO_LOGIN_PATH_ARR`（带 `/api/xxxapp` 前缀）与目标服务 `GlobalConfigure.NO_LOGIN_PATH_ARR`（不带前缀）并**重启网关和服务** |
| 403 / 无权限 | 该用户角色的资源授权里没有此接口 URL | 角色管理重新授权（接口权限 = 资源授权生成的 apiUrl 列表，URL 不带网关前缀） |
| 500 且访问的是超管专属路径 | ⚠️ 现状缺陷：网关 NotRoleException 分支在响应式环境取 Servlet 请求出错，非超管被拒时返回 500 而不是 403 | 当作无权限处理；换 superAdmin 验证 |
| 500 + CommonException 消息 | 业务异常（中文消息就是线索） | 按消息定位 Service 抛出点 |
| 500 + NPE | 常见：跨插件取到 null 未判空（JSONObject.getStr）；登录用户取不到（异步线程/定时任务里没有 token） | ObjectUtil.isEmpty 判空；无请求上下文的线程见树 E |
| 参数校验失败 | @Valid 注解的消息 | 看 Param 类校验消息 |
| delete 后台删不掉 | 逻辑删除字段值异常 | 检查 DELETE_FLAG 值（NOT_DELETE/DELETED） |
| SQL 报错 Unknown column | Entity 字段与表列不一致 / 排序字段没转下划线 | 核对 @TableName 大小写与列名；sortField 要 StrUtil.toUnderlineCase |

## 树 C：数据问题

| 现象 | 根因 |
|---|---|
| 列表查不到已插数据 | DELETE_FLAG 不是 NOT_DELETE（逻辑删除过滤掉了） |
| like 手机号查不到 | 该字段 SM4 加密落库，密文 like 永远不命中（见 crypto-sm） |
| 翻译字段（xxxName）为空 | @Trans 配置错：字典编码不存在 / target 表无数据 / 缺 @TableField(exist=false) 冗余字段 |
| 字典下拉为空 | 字典编码大小写不一致；前端字典缓存未刷新 |
| 新增后列表不刷新 | 前端没 emit('successful') / 没 tableRef.refresh |
| 时间范围查不到 | 前端没拆 startCreateTime/endCreateTime；或 value-format 缺失 |
| 分页总数不对 | PageParam 的 current/size 没传到（GET 参数绑定失败，检查字段名拼写） |
| ID 前端精度丢失 | 前端把 String id 当 Number 处理了（保持字符串） |
| 跨服务写入一半成功一半失败 | 没有分布式事务（无 Seata）：本服务 @Transactional 管不到 Feign 那头，需业务补偿或幂等设计 |

## 树 D：前端异常

| 现象 | 根因 |
|---|---|
| 白屏 | 控制台看报错；常见组件名拼错 / api js 路径 404 / 网关 9003 未启动（前端请求直接发往 `.env.development` 的 `VITE_API_BASEURL`，即网关） |
| 菜单不显示 | SYS_RESOURCE 菜单 SQL 未执行；或未给角色授权 |
| 按钮不显示 | hasPerm 码与 SYS_RESOURCE BUTTON 行 code 不一致（驼峰） |
| 表单打不开 | form.vue 忘了 defineExpose({ onOpen }) |
| 登录密码报错 | 前端 SM2 公钥（`src/utils/smCrypto.js`）与后端 `CommonCryptogramUtil.PUBLIC_KEY` 不一致 |
| 修改代码不生效 | Vite 热更新失效 → 重启 npm run dev；后端改动 → 重启对应服务（biz 代码重启 biz-app，平台插件重启 web-app，白名单/路由改动还要重启网关） |

## 树 E：网关、服务注册与跨服务调用

| 现象 | 根因 | 处理 |
|---|---|---|
| 经网关 404，直连服务正常 | 新 URL 前缀没登记：前端 `apiAdaptive.js` 没映射（请求没带 `/api/webapp`/`/api/bizapp`），或网关路由没有该前缀 | 先查前端 apiAdaptive.js 是否映射；新建服务再查网关路由（`snowy-gateway-app-dev.yaml` 的 routes，StripPrefix=2）。Knife4j 聚合只影响接口文档，不会导致 404 |
| 经网关 503 / Unable to find instance | 目标服务没注册到 Nacos：服务没起、启动失败，或 profile 不同导致命名空间/分组不一致（网关在 `snowy-cloud-dev` / `dev` 里找） | Nacos 控制台 → 服务管理看实例；核对各服务 `spring.profiles.active` |
| 免登录接口经网关仍 401 | 白名单只配了服务 GlobalConfigure | 网关 GatewayConfigure 补带前缀的条目，重启网关 |
| 直连服务 401、经网关也 401 | 白名单只配了网关 | 服务 GlobalConfigure 补不带前缀的条目，重启服务 |
| biz-app 启动报找不到 `XxxApi` 的 Bean | 该 Api 没有 api-context 远程代理（dev 侧 Dict/Log/Job/Push/WeakPassword、sys 侧 Menu/Button/Module 目前缺），或 biz-app pom 没引对应 `-feign` + `-api-context`（推导，使用前请先确认） | 补齐 `-feign`、`-feign-provider`、`-api-context` 三件，或改用已有 Api |
| Feign 调用对端报未登录 / 取不到登录用户 | 定时任务、@Async、消息消费等线程没有请求上下文，`FeignRequestInterceptor` 无头可透传，token 丢失 | 在有请求上下文的线程里调；或评估 mocker-token 插件的 `@CommonMockerToken`（默认未装配，使用前请先确认） |
| 登录时 ClassCastException 等怪错 / 登录后马上被踢 | 本地多个 Snowy 项目共用同一 Redis 库，token 名与 superAdmin ID 相同，读到了别的项目写的会话 | 本地各项目用不同 Redis 库号；改 web-app、biz-app、网关三份 Nacos 配置的 `spring.data.redis.database`（三者必须一致），重启并清浏览器 token |
| 网关认不出 web-app 签发的 token | 三个服务 Redis 地址/库号或 sa-token 配置不一致 | 对齐三份 Nacos 配置的 Redis 段与 sa-token 段 |
| biz 里写的定时任务报"找不到对应的类" | 定时任务调度（dev 插件）只装在 web-app，Runner 类必须在 web-app 类路径上 | Runner 放到 web-app 装配的模块；跑在 biz-app 的定时需求暂无现成方案 |
| biz 接口的操作日志查不到 | @CommonLog 落库切面只在 web-app 的 dev 插件里（推导，使用前请先确认） | 不要依赖 biz-app 的 @CommonLog 落库 |

## 排查工具箱

```bash
# 看后端日志：IDE 控制台为主；文件日志在启动目录下 logs/{应用名}-log/（如 logs/snowy-biz-app-log/log_error.log）
# 数据库直查（业务库 snowy-cloud；连接信息从 Nacos 配置 snowy-*-app-dev.yaml 的数据源段取）
mysql -h127.0.0.1 -uroot -p****** snowy-cloud -e "SELECT ID,DELETE_FLAG FROM BIZ_XXX LIMIT 5;"

# 服务注册情况（dev 命名空间 + dev 分组）
curl -s "http://localhost:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=20&groupName=dev&namespaceId=snowy-cloud-dev"

# 验证接口（Knife4j 网关聚合：http://localhost:9003/doc.html，basic 认证 snowyAdmin/Snowy@2026!，可带 token 调试）

# 全局搜代码
Grep pattern: "方法名/类名/错误消息片段"  path: snowy-plugin
```

**接口调试优先用 Knife4j（/doc.html）**：先登录拿 token（B 端鉴权里全局设置），再单测接口，把"前端问题还是后端问题"先切开；再用"直连服务端口 vs 经网关"把"网关问题还是服务问题"切开。

## 本项目特有问题库（高发 Top 12）

1. **登录一直报密码错误** → 本仓库出厂密码是 **Snowy@2026!**（不是网上说的 123456——那是官方演示站的）；权威来源 DEV_CONFIG 的 SNOWY_SYS_DEFAULT_PASSWORD_FOR_B。接口直调登录还需 SM2 加密密码（见 api-verify 技能），且走网关 `/api/webapp/auth/b/doLogin`
2. **Sa-Token 白名单改了没生效** → 网关 GatewayConfigure 与服务 GlobalConfigure 都是启动时构建的，两处都要改、两个都要重启
3. **授权了还是 403** → 授权的是按钮码，但接口权限要的是"资源授权勾选到对应菜单/按钮"生成的 apiUrl 集合；重新授权（在线会话会被刷新），仍不行让用户**重新登录**（权限码在 Redis 的 TokenSession 里）
4. **SM4 字段 like 查不到** → 设计期就要定查询方案
5. **雪花 id 丢失精度** → 后端 String，前端任何 Number 转换都会坏（parseInt 等）
6. **跨插件注入失败** → 只依赖了 *-api 却想注入实现类；或忘加依赖；在 biz-app 还要有对应 `-feign` + `-api-context`
7. **@Trans 不生效** → autoResultMap 没开 / 字典缓存旧数据（发数据变更事件）
8. **新增菜单 404** → 菜单 component 路径与前端 views 目录不匹配（如 biz/supplier/index ↔ src/views/biz/supplier/index.vue）
9. **生成的代码包名错** → 代码生成器 packageName/pluginName 填错，落位后要手工核（生成到 `snowy-plugin/{插件}/{插件}-func/src/main/java`）
10. **新业务前缀经网关 404** → apiAdaptive.js 没登记；新建服务时网关路由没加
11. **Nacos 配置改了不生效 / 改错地方** → 改的是仓库 `config-data/*.yaml` 源文件但没同步进 Nacos；或 Nacos 里改了别的命名空间
12. **本地登录 ClassCastException** → 多个 Snowy 项目共用同一 Redis 库串会话（见树 E）

## 检查清单（修复后）

- [ ] 根因明确（能说出为什么），不是碰巧好使
- [ ] 修复未引入新的规范违规（过一遍 code-patterns）
- [ ] 同类隐患点已排查（同类接口/同类字段；白名单两处、前缀登记是否一致）

## 参考实现（排查时看这些）

| 文件 | 说明 |
|---|---|
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/handler/GlobalExceptionHandler.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/handler/GlobalExceptionHandler.java` | 异常→错误码映射（两个服务各一份） |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/core/util/GlobalExceptionUtil.java`、`AuthExceptionUtil.java` | 网关侧异常转换 |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关白名单/拦截规则 |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` | 服务白名单/拦截规则 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/` | 全部环境配置（数据源/Redis/网关路由/Sa-Token） |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/interceptor/FeignRequestInterceptor.java` | Feign 请求头透传 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/FeignConstant.java` | Feign 服务名常量 |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端 URL 前缀映射 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/log/` | 操作/异常日志模块 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/monitor/` | 服务器监控 |
