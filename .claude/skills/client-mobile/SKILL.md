---
name: client-mobile
description: Snowy C 端（client 插件）与移动端（mobile 插件）开发规范：C 端用户体系、C 端登录链路、clientRequest 前端封装、移动端资源管理、代码生成器 mobile 模板；微服务版 client-func 与 mobile-func 都运行在 web-app，C 端接口经网关 /api/webapp 前缀。触发场景：1) 开发面向终端用户（C 端）的功能 2) 小程序/APP/H5 接口对接 3) 移动端菜单资源管理 4) C 端接口经网关 401/404、网关 CLIENT 组白名单。触发词：C端、客户端、client、移动端、mobile、uni-app、小程序、APP、CLIENT_USER、移动端菜单、C端用户、网关、/api/webapp、C端白名单、CLIENT_USER_PERMISSION_PATH_ARR。
---

# Snowy C 端与移动端规范

## B 端 vs C 端（先分清）

| | B 端（管理后台） | C 端（终端用户） |
|---|---|---|
| 用户表 | SYS_USER | CLIENT_USER |
| 插件 | sys / biz | client |
| 运行服务 | sys 在 web-app、biz 在 biz-app | client-func 与 C 端认证（auth-func）都在 **web-app** |
| 登录 | `/auth/b/doLogin` | `/auth/c/doLogin`（见 AuthClientController） |
| 经网关地址 | `/api/webapp/sys/...`、`/api/bizapp/biz/...` | `/api/webapp/auth/c/...`、`/api/webapp/client/c/...` |
| 工具类 | StpUtil / StpLoginUserUtil | StpClientUtil / StpClientLoginUserUtil（均在 snowy-plugin-auth-api，两个服务都可用） |
| 前端请求 | request.js baseRequest | clientRequest.js（现状见下文） |
| 前端工程 | snowy-admin-web | 外部 H5/小程序/APP（uni-app） |

C 端接口路径规范：`/client/c/{业务域}/{动作}`（被 CLIENT_USER_PERMISSION_PATH_ARR 路由规则覆盖，走 C 端鉴权）。

**C 端鉴权是两层**（都要对上）：

| 层 | 位置 | C 端路径组（CLIENT_USER_PERMISSION_PATH_ARR） |
|---|---|---|
| 网关 SaReactorFilter | `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | `/api/webapp/auth/c/**`、`/api/webapp/client/c/**`（带网关前缀） |
| 服务 SaServletFilter | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`（biz-app 同名类同样配置） | `/auth/c/**`、`/client/c/**`（不带前缀） |

新增 C 端**免登录**接口：网关 `NO_LOGIN_PATH_ARR`（带 `/api/webapp` 前缀）+ 目标服务 `GlobalConfigure`（不带前缀）两处都要加。

## C 端模块结构（snowy-plugin-client）

```
snowy-plugin/snowy-plugin-client/
├── snowy-plugin-client-api/     ClientUserApi 接口
└── snowy-plugin-client-func/    vip.xiaonuo.client.modular
    ├── user/        C 端用户（ClientUser，CLIENT_USER 表：账号/头像/昵称/状态等）
    └── relation/    C 端关系（好友/关注等社交关系）
```

新增 C 端业务：在 client 插件 `snowy-plugin-client-func` 的 modular 下建域，Controller URL 用 `/client/c/{域}/{动作}`，注入用 StpClientUtil 取 C 端登录用户。结构规范与 biz 完全相同（六件套），只是类前缀 Client。

⚠️ 微服务注意（推导，使用前请先确认）：
- client 插件只有 `-api` 和 `-func`，**没有 feign / api-context**，biz-app 里注入 `ClientUserApi` 会缺 Bean
- 前端 apiAdaptive 把含 `/client/` 的地址都映射到 `/api/webapp`，网关 C 端组也只覆盖 `/api/webapp/...`；C 端业务若放到 biz-app，需要同步改 apiAdaptive 映射与网关 C 端组
- 网关 Knife4j 的 `SNOWY-PLUGIN-CLIENT` 分组指向 biz-app（`/api/bizapp/v3/api-docs/SNOWY-PLUGIN-CLIENT`），而 client-func 实际在 web-app，C 端接口文档可能显示为空；C 端登录接口在 `SNOWY-PLUGIN-AUTH` 分组

## C 端登录链路（auth 插件已实现）

```
POST /auth/c/doLogin（经网关 /api/webapp/auth/c/doLogin，已在免登录名单；另有 doLoginByPhone / doLoginByEmail / doLoginByOtp）
   → 校验 CLIENT_USER
   → StpClientUtil.login(id)（独立 StpLogic，与 B 端 token 隔离）
   → 返回 C 端 token（会话存共享 Redis，网关与各服务都能校验）
```

参考：`snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/`（AuthClientController + AuthServiceImpl 的 client 分支）。

## 前端 C 端请求（如需在 admin-web 调 C 端接口）

```js
import { clientBaseRequest } from '@/utils/clientRequest'
// 用法与 baseRequest 相同，token 取 CLIENT_TOKEN，失效跳 /front/client/login
```

⚠️ 现状：`clientRequest.js` 的 `clientBaseRequest` **不经 `convertUrl`**（baseURL 固定 `/api`），且目前没有任何文件引用；直接请求 `/client/c/xxx` 会缺 `/api/webapp` 网关前缀（推导：经 vite 代理到网关后 404）。出厂 admin-web 内的 C 端页面（`views/auth/client/`、`views/front/`）实际用的是 `baseRequest`（会经 apiAdaptive 加前缀）。

实际 C 端页面通常在独立 uni-app 工程里（用 axios/fly 自行封装，token 头名 `token`，baseURL 指向**网关** `http://localhost:9003`，接口路径带 `/api/webapp` 前缀）。

## 移动端（snowy-plugin-mobile）

```
snowy-plugin/snowy-plugin-mobile/snowy-plugin-mobile-func/src/main/java/vip/xiaonuo/mobile/modular/
├── mobile/     MobileApiProvider（对外接口实现）
└── resource/   移动端模块/菜单/按钮（MobileModule / MobileMenu / MobileButton，MOBILE_RESOURCE 表）
```

- 作用：管理 uni-app 端的菜单与按钮权限（与 B 端 SYS_RESOURCE 平行的一套）；mobile-func 运行在 web-app，接口经网关 `/api/webapp/mobile/...`
- biz-app 已装配 mobile-feign + mobile-api-context，可注入 mobile 的 Api 接口远程调用
- 内置移动端图片资源：`snowy-plugin/snowy-plugin-mobile/snowy-plugin-mobile-func/src/main/resources/mobile/`（fileImg、swiper）
- 代码生成器可产出 mobile 代码：GenBasic 配置移动端模块（mobileModuleId）后，sqlend 模板会生成 MOBILE_RESOURCE 的 INSERT（按钮码 `mobile{ClassName}Add` 形态）
- 完整 uni-app 前端工程在独立仓库（snowy 官方 mobile 工程），本仓库不含源码

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| C 端接口用 StpUtil 取用户 | StpClientUtil / StpClientLoginUserUtil（取错端会拿到 null） |
| C 端接口路径写 /client/b/... | `/client/c/...`（走 C 端路由规则） |
| C 端免登录接口只改服务端 GlobalConfigure | 网关 NO_LOGIN_PATH_ARR（带前缀）+ 服务端 GlobalConfigure 两处都改 |
| uni-app 的 baseURL 指向 web-app 9101 | 指向网关 9003，路径带 `/api/webapp` |
| CLIENT_USER 与 SYS_USER 混用 | 两套独立体系，外键别串 |
| 移动端按钮码用 B 端驼峰码 | mobile 前缀码（MOBILE_RESOURCE） |
| 在 B 端管理页调 clientRequest | B 端一律 baseRequest |

## 检查清单

- [ ] C 端接口前缀 /client/c/，取用户用 StpClientUtil 系
- [ ] C 端表用 CLIENT_ 前缀，类前缀 Client
- [ ] C 端白名单 / C 端组在网关与服务端两层一致
- [ ] 移动端资源走 MOBILE_RESOURCE（生成器 mobileModuleId 配置）
- [ ] 两端 token 不混用

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-client/snowy-plugin-client-func/src/main/java/vip/xiaonuo/client/modular/user/` | C 端用户六件套 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/controller/AuthClientController.java` | C 端登录接口 |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关 NO_LOGIN / SUPER / CLIENT 三组路径 |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java` | web-app 服务端白名单与 C 端组 |
| `snowy-plugin/snowy-plugin-mobile/snowy-plugin-mobile-func/src/main/java/vip/xiaonuo/mobile/modular/` | 移动端资源管理 |
| `snowy-admin-web/src/utils/clientRequest.js` | C 端请求封装 |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/mobile/` | 移动端代码模板 |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/sqlend/Mysql.sql.btl` | MOBILE_RESOURCE SQL 模板 |
