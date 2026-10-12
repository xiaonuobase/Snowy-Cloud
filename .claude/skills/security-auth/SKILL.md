---
name: security-auth
description: Snowy 鉴权与安全规范：Sa-Token B/C 双端模型、路由白名单三段、登录方式、获取当前用户、接口权限与按钮权限、数据范围、三方登录、SSO。触发场景：1) 接口需要登录/免登录/权限校验 2) 获取当前登录用户信息 3) 数据范围（按机构过滤）4) 三方登录或 SSO 集成。触发词：鉴权、权限、登录、Sa-Token、StpUtil、token、白名单、免登录、401、403、数据范围、DataScope、角色、三方登录、SSO、OAuth2、越权、网关鉴权、GatewayConfigure、SaReactorFilter、跨服务会话、Feign 接口暴露。
---

# Snowy 鉴权与安全规范

## 双端模型（B 端 / C 端）

| 端 | 用户体系 | 登录工具类 | 接口前缀（服务内 / 经网关） |
|---|---|---|---|
| B 端（管理后台） | SYS_USER（sys 插件） | `StpUtil` / `StpLoginUserUtil.getLoginUser()` | `/sys/*` `/dev/*` `/gen/*` `/auth/b/*` → 网关 `/api/webapp/...`；`/biz/*` → 网关 `/api/bizapp/...` |
| C 端（终端用户） | CLIENT_USER（client 插件，运行在 web-app） | `StpClientUtil` / `StpClientLoginUserUtil` | `/auth/c/**` `/client/c/**` → 网关 `/api/webapp/auth/c/**` `/api/webapp/client/c/**` |

两套独立 StpLogic（`stpLogic` 与 `stpClientLogic`），token 互不通用。注册位置有三处：web-app 走 auth 插件的 `AuthConfigure`，biz-app 走自己的 `biz/core/config/AuthConfigure`，网关在 `GatewayConfigure` 里注册。**业务后台代码一律用 B 端 API**。

## 两层鉴权（网关 + 服务，都生效）

| 层 | 位置 | 机制 | 路径写法 |
|---|---|---|---|
| 网关层 | `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | `SaReactorFilter`（响应式） | **带**网关前缀：`/api/webapp/...`、`/api/bizapp/...` |
| 服务层 | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` | `SaServletFilter`；另有 `SaInterceptor` 处理 `@SaCheckPermission` 等注解 | **不带**前缀：`/biz/...`、`/sys/...` |

**会话跨服务共享**：登录在 web-app 完成，Sa-Token 会话（含 `loginUser`，里面有权限码、角色码、数据范围）写入 Redis（Sa-Token alone-redis，与业务缓存同为 database 1）。网关和 biz-app 都从同一个 Redis 读会话来判登录、取权限，所以三者的 Redis 地址/库号、`sa-token.token-name`（`token`）、timeout 必须一致（都在各自的 Nacos 配置 `snowy-*-app-dev.yaml` 里）。

## 路由级白名单（两处都要改，改后必须重启）

两个位置各有同名的三个数组：

| 数组 | 作用 |
|---|---|
| `NO_LOGIN_PATH_ARR` | 免登录路径（验证码、登录接口、公开页） |
| `CLIENT_USER_PERMISSION_PATH_ARR` | C 端鉴权路径 |
| `SUPER_PERMISSION_PATH_ARR` | 仅超管角色可访问 |

新增公开接口 → **网关 GatewayConfigure 加带前缀的路径（如 `/api/bizapp/biz/xxx/publicInfo`）+ 目标服务 GlobalConfigure 加不带前缀的路径（如 `/biz/xxx/publicInfo`）** → 重启网关和该服务。漏任一处 = 401。

> ⚠️ 现状：两处名单并不完全一致（例如服务侧放行了 `/auth/b/doLoginByThirdToken`、`/auth/third/bindAccount`，网关侧没有对应的 `/api/webapp/...` 条目），改动前先把两边对照着看。

## 获取当前用户（业务代码高频）

```java
import cn.dev33.satoken.stp.StpUtil;
import vip.xiaonuo.auth.core.util.StpLoginUserUtil;

String userId = StpUtil.getLoginIdAsString();                // 当前用户 id
SaBaseLoginUser user = StpLoginUserUtil.getLoginUser();      // 完整登录用户（name/orgId/...），从 Redis 会话读取
String orgId = StpLoginUserUtil.getLoginUser().getOrgId();
// StpLoginUserUtil、SaBaseLoginUser 在 snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-api 的 auth/core/util、auth/core/pojo 下
// biz-app 只装了 auth-api（未装 auth-func），上面两个类可直接用
```

自动填充的 createUser/updateUser 就是取自当前登录 id（各服务 GlobalConfigure 里的 MetaObjectHandler）。

> 无请求上下文的线程（定时任务、@Async、消息消费）里没有 token：本服务取不到登录用户，经 Feign 调别的服务时也不会带 token。mocker-token 插件的 `@CommonMockerToken` 是为此准备的，但默认未装配到任何应用，使用前请先确认。

## 接口权限 vs 按钮权限（两层，别混淆）

| 层 | 载体 | 形态 | 用法 |
|---|---|---|---|
| 接口权限 | 角色授权生成的数据范围 apiUrl | **URL 式（不带网关前缀）** | 后端 `@SaCheckPermission("/biz/xxx/page")` |
| 按钮权限 | SYS_RESOURCE 的 BUTTON 行 code | **驼峰式** | 前端 `hasPerm('bizXxxAdd')` |

登录时（web-app 内 sys 插件的 `SysLoginUserApiProvider`）组装：`permissionCodeList`（来自 dataScope 的 apiUrl）+ `buttonCodeList`（驼峰码下发前端），随会话存入 Redis，biz-app 校验 `@SaCheckPermission` 时读的就是它。所以：
- 新接口上线 → 角色管理授权对应资源，否则非超管 403
- 新按钮 → SYS_RESOURCE 加 BUTTON 行 + 授权，否则前端不显示
- 角色授权变更时 sys 插件会调用 refreshOnlineUserPermission 刷新 Redis 里的在线会话；仍不生效再让用户重新登录

## 数据范围（DataScope）

- 用户可被授权"某模块/某菜单下、某机构的全部/自定义/仅本部门/仅本人"数据范围（SYS_USER_DATA_SCOPE 表，角色管理→数据授权界面配置）
- 后端查询受数据范围过滤的实现走 sys 插件的 ApiUrl 清单机制：数据范围授权时选择"接口地址集合"；`StpLoginUserUtil.getLoginUserDataScope()` 按当前请求的 servletPath（网关已去掉前缀）匹配
- **业务表要支持数据范围**：确保查询走标准 QueryWrapper 链路；机构维度字段命名 orgId（varchar，SYS_ORG 外键）
- 手动忽略数据权限的场景参考 DataPermissionHelper 类似机制（以 sys 插件实现为准）

## 登录方式矩阵（auth 插件已实现，直接用）

账密（密码 SM2 加密传输 + SM3 摘要校验）、手机验证码、邮箱验证码、OTP 动态口令、三方 token 换绑。入口：`snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/controller/AuthController.java`（B 端 `/auth/b/*`）与 `AuthClientController`（C 端 `/auth/c/*`），都运行在 web-app，经网关为 `/api/webapp/auth/...`。

## 三方登录与 SSO

- 三方登录：JustAuth（`auth/modular/third/`），支持微信/钉钉/企微/飞书/QQ/微博/Gitee 等；配置在 DEV_CONFIG
- SSO/OAuth2/OIDC/CAS/SAML：`auth/core/protocol/`（服务端与客户端协议栈）；配置 `auth/modular/sso/config/AuthSsoConfigure`
- 回调地址对外暴露的是网关地址（`/api/webapp/auth/...`），白名单同样两处都要有
- 业务一般只做"绑定/解绑三方账号"，协议栈不用动

## ⚠️ 已知现状（只提醒，不要当作推荐做法）

- **网关 NotRole 返回 500**：非超管经网关访问 `SUPER_PERMISSION_PATH_ARR` 中的接口时，网关 `AuthExceptionUtil` 的 NotRoleException 分支在响应式环境里调用 `CommonServletUtil.getRequest()`，结果返回 500 而不是 403。排查时把"超管路径 + 非超管 + 500"当成无权限处理
- **`/feign/**` 暴露风险（推导，未验证）**：服务侧 GlobalConfigure 把 `/feign/**` 列为免登录，feign-provider 上没有 `@SaCheckPermission`，网关也没有拦截 `/api/*/feign/**`，因此登录用户可能经网关直接调用内部 Feign 接口。新写 feign-provider 时不要放敏感写操作；生产部署前评估是否需要在网关屏蔽该路径

## 安全检查清单

- [ ] 新公开接口已同时加网关 GatewayConfigure（带前缀）与目标服务 GlobalConfigure（不带前缀），并重启两者
- [ ] @SaCheckPermission 值 = 服务内 URL（不带 /api/xxxapp 前缀）；新资源已授权给角色
- [ ] 涉及用户数据的接口校验归属（防水平越权：编辑/删除前确认记录属于当前用户机构）
- [ ] 密码类字段不落日志、不明文存储（见 crypto-sm）
- [ ] 敏感查询参数防注入（checkSqlInjection）
- [ ] 新增的 feign-provider 接口没有暴露敏感写操作

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| `@SaCheckPermission("biz:xxx:list")` | `@SaCheckPermission("/biz/xxx/page")` |
| `@SaCheckPermission("/api/bizapp/biz/xxx/page")`（带了网关前缀） | `@SaCheckPermission("/biz/xxx/page")` |
| 只在服务 GlobalConfigure 加了免登录，经网关仍 401 | 网关 GatewayConfigure 同步加 `/api/xxxapp/...` 条目 |
| Controller 里手写 `StpUtil.getLoginId()` 判空跳转 | 白名单/注解体系（GatewayConfigure + GlobalConfigure + @SaCheckLogin 系） |
| B 端接口用 StpClientUtil | B 端 StpUtil / StpLoginUserUtil |
| 新菜单配好但用户 403 | 角色管理未授权（接口权限来自资源授权） |
| 自己写 token 解析 | Sa-Token 体系（token 名 `token`，请求头） |
| 各服务 Redis 库号不一致 | web-app、biz-app、网关同一个 Redis 库，否则网关/biz-app 认不出 web-app 发的 token |

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关白名单（带前缀）+ SaReactorFilter + 网关侧 StpLogic/StpInterfaceImpl |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/core/util/AuthExceptionUtil.java` | 网关鉴权异常转换（NotRole 500 现状所在） |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java` | web-app 白名单 + SaServletFilter（含锁屏校验） |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` | biz-app 白名单 + SaServletFilter |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/AuthConfigure.java` | biz-app 双端 StpLogic + StpInterfaceImpl + SaInterceptor |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/core/config/AuthConfigure.java` | web-app 双端 StpLogic + StpInterfaceImpl + SaInterceptor |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/provider/SysLoginUserApiProvider.java` | 权限码/按钮码/数据范围组装（refreshOnlineUserPermission） |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/` | 登录全流程 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-api/src/main/java/vip/xiaonuo/auth/core/pojo/SaBaseLoginUser.java` | 登录用户 POJO |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-api/src/main/java/vip/xiaonuo/auth/core/util/StpLoginUserUtil.java` | 取当前用户、数据范围 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/third/` | 三方登录 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/core/protocol/` | SSO 协议栈 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/` | 各服务 sa-token 段与 Redis 配置 |
