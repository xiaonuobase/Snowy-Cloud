---
name: api-development
description: Snowy 接口设计规范：URL 命名、参数与返回、CommonResult、CommonException 异常体系、Knife4j 文档、白名单与权限。触发场景：1) 设计新接口或修改接口 2) 处理统一返回/异常/错误码 3) 接口需要免登录或加权限。触发词：API、接口、RESTful、URL、Controller、CommonResult、CommonException、异常、错误码、doc.html、Knife4j、Swagger、白名单、网关、网关前缀、跨服务、feign、Nacos。注意：CRUD 全套代码模板见 crud-development；Sa-Token 体系详解见 security-auth。
---

# Snowy API 开发规范

## URL 命名规范

格式：`/{插件}/{业务域}/{动作}`，**动词式全路径直接写在方法注解上**，类上不写 @RequestMapping。

| 动作 | HTTP | 路径示例 | 参数形式 |
|---|---|---|---|
| 分页 | GET | `/biz/xxx/page` | Param 对象（无 @RequestBody） |
| 列表(不分页) | GET | `/biz/xxx/list` | Param 对象 |
| 详情 | GET | `/biz/xxx/detail` | `@Valid XxxIdParam` |
| 添加 | POST | `/biz/xxx/add` | `@RequestBody @Valid XxxAddParam` |
| 编辑 | POST | `/biz/xxx/edit` | `@RequestBody @Valid XxxEditParam` |
| 删除 | POST | `/biz/xxx/delete` | `@RequestBody @Valid @NotEmpty List<XxxIdParam>` |
| 业务动作 | POST | `/biz/xxx/disableStatus` | `@RequestBody @Valid XxxIdParam` |
| 导出 | GET | `/biz/xxx/export` | void + HttpServletResponse |
| 下载 | GET | `/biz/xxx/download` | void + HttpServletResponse |

规则：
- **查询 GET + Param 对象**（Spring 自动绑定 query string），**写入 POST + @RequestBody @Valid**
- ❌禁止 RESTful 路径变量（`/user/{id}`）、PUT/DELETE 方法
- 插件前缀：sys / biz / dev / gen / mobile；C 端接口 auth 插件用 `/auth/c/**`、client 插件用 `/client/c/**`（auth-func、client-func 都装在 web-app，经网关为 `/api/webapp/auth/c/**`、`/api/webapp/client/c/**`）

### 网关前缀（微服务版）

Controller 上**只写** `/{插件}/{业务域}/{动作}`，网关前缀由网关路由加/去，代码里不出现：

| 插件 URL 前缀 | 所在服务（端口） | 经网关 9003 的访问前缀 |
|---|---|---|
| `/sys/` `/auth/` `/dev/` `/gen/` `/mobile/` `/client/` | snowy-web-app（9101） | `/api/webapp`（如 `/api/webapp/sys/user/page`） |
| `/biz/` | snowy-biz-app（9102） | `/api/bizapp`（如 `/api/bizapp/biz/notice/page`） |

- 网关路由在 Nacos 配置 `snowy-gateway-app-dev.yaml`：`/api/webapp/**` → `lb://snowy-web-app`、`/api/bizapp/**` → `lb://snowy-biz-app`，都带 `StripPrefix=2`（去掉前两段再转发）
- 前端由 `snowy-admin-web/src/utils/apiAdaptive.js` 按 URL 前缀自动补网关前缀；新的 URL 前缀要在这里登记，否则经网关 404

## 统一返回 CommonResult

```java
// snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonResult.java
CommonResult.data(obj)          // 成功带数据：{code:200, msg:"操作成功", data:obj, traceId:...}
CommonResult.ok()               // 成功无数据（写操作）
CommonResult.error("消息")       // 失败（一般不直接用，业务失败应抛 CommonException）
CommonResult.get(code, msg, data)  // 底层构建
```

- Controller 方法返回类型一律 `CommonResult<T>`；例外：文件流/导出/下载为 `void` + `HttpServletResponse` 参数（参考 SysUserController.exportUser）
- ❌禁止返回 Map<String,Object>、裸 Entity 列表不带包装、R/AjaxResult/Result 等其他包装类

## 异常体系

```java
// 业务校验失败 —— 抛 CommonException（支持 {} 占位符格式化，消息中文）
throw new CommonException("存在重复的账号，账号为：{}", account);
throw new CommonException("XXX不存在，id值为：{}", id);

// 系统错误码 —— 枚举定义
throw new CommonException(CommonExceptionEnum.XXX);   // 需要新错误码时在 CommonExceptionEnum 增加
```

- ❌禁止 `RuntimeException`、`IllegalArgumentException`、`ServiceException` 直接抛出
- 全局异常处理**每个服务各有一份**（@ControllerAdvice → GlobalExceptionUtil.getCommonResult(e)）：web-app 在 `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/handler/GlobalExceptionHandler.java`，biz-app 在 `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/handler/GlobalExceptionHandler.java`；业务代码不要自己 try-catch 后返回错误 CommonResult
- 参数校验失败（@Valid 不通过）由全局处理器统一转为中文提示，不需要手写校验返回
- 跨服务调用失败：服务端异常响应为 HTTP 500，调用方收到 FeignException；biz-app 的 GlobalExceptionUtil 对 FeignException 直接返回 `e.getMessage()`（含远程地址与响应体），不会还原成对方的 CommonException 消息。Feign 无 fallback

## 文档注解（Knife4j / OpenAPI3）

```java
@Tag(name = "XXX控制器")                    // 类级
@Operation(summary = "获取XXX分页")          // 方法级
@Schema(description = "标题")                // 字段级（Param/Entity/Result）
@Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)  // 必填字段
```

接口文档地址：`http://localhost:9003/doc.html`（**网关聚合**，basic 认证 snowyAdmin/Snowy@2026!，配置在 Nacos `snowy-gateway-app-dev.yaml` 的 `knife4j`；各服务自身 `knife4j.basic.enable=false`）。所有 description 用中文。

分组共 7 个，由两层配置拼起来：

| 分组 | springdoc 分组配置所在 | 网关聚合路由 url |
|---|---|---|
| SNOWY-PLUGIN-AUTH / DEV / GEN / MOBILE / SYS | `snowy-web-app-dev.yaml` 的 `springdoc.group-configs`（按包 `vip.xiaonuo.{插件}` 扫描） | `/api/webapp/v3/api-docs/{分组}` |
| SNOWY-PLUGIN-BIZ / CLIENT | `snowy-biz-app-dev.yaml` 的 `springdoc.group-configs` | `/api/bizapp/v3/api-docs/{分组}` |

- biz 插件新业务域只要包在 `vip.xiaonuo.biz` 下，自动进 SNOWY-PLUGIN-BIZ 分组，无需改配置
- 新插件/新包：在所在服务的 Nacos 配置加 springdoc 分组 + 在网关 Nacos 配置 `knife4j.gateway.routes` 加一条聚合路由
- Nacos 配置的仓库源文件在 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/`，运行时以 Nacos 控制台（`http://localhost:8848/nacos`）里的同名配置为准
- ⚠️ 注意：CLIENT 分组配在 biz-app，但 client-func 实际装在 web-app，该分组可能为空（推导，使用前请先确认）

## 权限注解

```java
@SaCheckPermission("/biz/xxx/page")     // 值 = Controller 上的接口 URL（不是冒号式权限码！也不带 /api/bizapp 网关前缀）
```

- 用户能通过校验的条件：其角色的资源授权生成的数据范围 apiUrl 列表包含该 URL
- 校验发生在服务内（biz-app 由 `AuthConfigure` 注册 SaInterceptor），权限数据从 Redis 里 Sa-Token 会话的 loginUser 读取；网关只做登录/超管/C 端的路由级校验
- 新接口上线后必须在"角色管理 → 授权"里勾选对应资源（菜单按钮走"授权资源"，接口 URL 走"授权权限"），否则非超管用户 403
- 超管角色：`SUPER_PERMISSION_PATH_ARR` 里的路径只有超管能访问；但 `@SaCheckPermission` 的接口权限同样来自授权关系——种子数据给超管角色逐条授了出厂接口（SYS_RELATION 的 SYS_ROLE_HAS_PERMISSION），生成器 PRO 方式也会自动给超管授新接口（`SysRoleApiProvider.grantForGenPermission`）。**手写的新接口要给超管角色也"授权权限"**，否则超管调用同样无权限（注意：未发现超管绕过逻辑，使用前请先确认）
- ⚠️ 注意（推导，使用前请先确认）：web-app 与 biz-app 启动时都把本服务的接口清单**整体覆盖**写入同一个 Redis 缓存键 `permission-resource`，"授权权限"弹窗读的就是它，清单里可能只剩最后启动的那个服务的接口；找不到 `/biz/...` 接口时先重启 biz-app 再打开弹窗

## 免登录 / 白名单

微服务版鉴权**网关和服务两层都生效**，路由级放行必须**两处都改**：

| 位置 | 文件 | 路径写法 |
|---|---|---|
| 网关（SaReactorFilter） | `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | **带**网关前缀，如 `/api/bizapp/biz/xxx/publicList` |
| 目标服务（SaServletFilter） | web-app：`snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`；biz-app：`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` | **不带**前缀，如 `/biz/xxx/publicList` |

两处文件里都有同名的三组数组：

| 数组 | 含义 | 修改时机 |
|---|---|---|
| `NO_LOGIN_PATH_ARR` | 免登录路径 | 新增公开接口（如验证码、健康检查）时加这里 |
| `CLIENT_USER_PERMISSION_PATH_ARR` | C 端鉴权路径（/auth/c/**、/client/c/**） | 新增 C 端接口模块时 |
| `SUPER_PERMISSION_PATH_ARR` | 仅超管可访问路径 | 新增敏感管理接口时 |

改完白名单必须重启网关和对应服务才生效。只改一处时，另一层仍会拦截返回未登录。

> ⚠️ 已知缺陷：网关上非超管访问 `SUPER_PERMISSION_PATH_ARR` 里的路径时，网关 AuthExceptionUtil 的 NotRoleException 分支在响应式环境里调 `CommonServletUtil.getRequest()`，会返回 500 而不是 403。
> ⚠️ 已知风险（推导未验证）：各服务 `NO_LOGIN_PATH_ARR` 里都有 `/feign/**`，feign-provider 无 @SaCheckPermission，网关也未拦截 `/api/*/feign/**`，登录用户可经网关直调 feign 接口。

## 防重复提交

```java
@CommonNoRepeat                          // snowy-common 注解，写操作防重复提交
@PostMapping("/biz/xxx/add")
public CommonResult<String> add(...) { ... }
```

关键写操作（下单、支付类）建议加；普通 CRUD 可不加（Controller 已有 @Validated + 前端按钮防抖）。

## 跨服务接口返回值约定（微服务版）

服务之间不走 Controller，走"Api 接口 → Feign → 远程代理"链路（详见 crud-development 的 feign 化步骤）：

| 层 | 位置 | 返回值约定 |
|---|---|---|
| `XxxApi` 接口 | `snowy-plugin/{插件}/{插件}-api`，包 `vip.xiaonuo.{插件}.api` | `JSONObject` / `List<JSONObject>` / `Page<JSONObject>` / `String` / 基本类型，**不返回 Entity**（平台里 AuthInnerApi 返回 CommonResult 属特例，业务接口不要仿） |
| `XxxFeign` | `{插件}-feign`，`@FeignClient(name = FeignConstant.WEB_APP 或 BIZ_APP, contextId = "XxxFeign")` | 路径 `/feign/{插件}/{域}/{方法}`；对象/集合一律返回 `String`（JSON），基本类型可直接返回 |
| `XxxFeignProvider` | `{插件}-feign-provider`，`@RestController implements XxxFeign` | 转调本地 XxxApi，用 `JSONUtil.toJsonStr` 序列化 |
| `XxxApiContextBean` | `{插件}-api-context`，`@Component implements XxxApi` | 调 Feign 后用 `JSONUtil.parseObj` / `toList` 还原成 XxxApi 声明的类型 |

- 业务代码只注入 `XxxApi`，不直接注入 `XxxFeign`
- `/feign/**` 不是对外接口：不加 @SaCheckPermission、不进菜单授权、前端不调用
- Feign 请求拦截器（各应用 `core/interceptor/FeignRequestInterceptor`）透传当前请求的全部请求头和 traceId；没有请求上下文的线程（定时任务、异步线程）调 Feign 时拿不到 token
- 无 Feign fallback、无分布式事务（未接 Seata）：远程写操作不受本地 `@Transactional` 回滚控制

## 常见错误正误对照

| ❌ 错误 | ✅ 正确 |
|---|---|
| `@RequestMapping("/biz/xxx")` + `@GetMapping("/{id}")` | 方法上直接 `@GetMapping("/biz/xxx/detail")` |
| `@PutMapping` / `@DeleteMapping` | 全部用 `@PostMapping` |
| `public Map<String, Object> detail(...)` | `public CommonResult<BizXxx> detail(...)` |
| `try { ... } catch (Exception e) { return CommonResult.error(e.getMessage()); }` | 直接抛 CommonException，交给全局处理器 |
| `throw new RuntimeException("xxx不存在")` | `throw new CommonException("xxx不存在，id值为：{}", id)` |
| 白名单写死在 Controller 里判断 | 网关 GatewayConfigure（带前缀）+ 目标服务 GlobalConfigure（不带前缀）两处的 NO_LOGIN_PATH_ARR 都加 |
| `@GetMapping("/api/bizapp/biz/xxx/page")` / `@SaCheckPermission("/api/bizapp/...")` | 方法上只写 `/biz/xxx/page`，网关前缀不进代码 |
| 跨服务接口返回 Entity 或 CommonResult | XxxApi 返回 `JSONObject` / `List<JSONObject>`，Feign 层返回 JSON 字符串 |

## 检查清单

- [ ] URL = /{插件}/{域}/{动作}，动词式，写方法注解上，不带网关前缀
- [ ] 查询 GET+Param / 写入 POST+@RequestBody @Valid
- [ ] 返回 CommonResult<T>（文件流 void 除外）
- [ ] @SaCheckPermission 值与 URL 完全一致
- [ ] @Tag/@Operation/@Schema 中文齐全
- [ ] 业务异常用 CommonException，无裸 RuntimeException
- [ ] 新公开接口已加网关 GatewayConfigure + 目标服务 GlobalConfigure 两处白名单，并说明需重启网关与该服务
- [ ] 新 URL 前缀已在前端 apiAdaptive.js 登记；经网关 `http://localhost:9003/api/...` 能访问
- [ ] 跨服务接口（XxxApi/Feign）不返回 Entity，业务代码只注入 XxxApi

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/controller/BizNoticeController.java` | 标准 Controller 范本 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/controller/SysUserController.java` | 含导出/下载 void 场景 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonResult.java` | 统一返回体 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/exception/CommonException.java` | 业务异常 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/enums/CommonExceptionEnum.java` | 错误码枚举 |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/handler/GlobalExceptionHandler.java` | web-app 全局异常处理 |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/handler/GlobalExceptionHandler.java` | biz-app 全局异常处理（GlobalExceptionUtil 含 FeignException 分支） |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关路由白名单（带前缀） |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java` | web-app 路由白名单（不带前缀） |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` | biz-app 路由白名单（不带前缀） |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-gateway-app-dev.yaml` | 网关路由 + Knife4j 聚合路由与 basic 认证 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml`、`snowy-biz-app-dev.yaml` | 各服务 springdoc 接口文档分组 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-feign/src/main/java/vip/xiaonuo/sys/feign/SysUserFeign.java` | Feign 接口路径与返回值范本 |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端 URL 前缀 → 网关前缀映射 |
