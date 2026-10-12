---
name: plugin-architecture
description: Snowy 微服务版插件化架构与跨插件/跨服务调用规范：Maven 模块拓扑（base/modules/plugin/server）、插件五类子模块、插件内 core/modular 两层结构、跨服务调用五件套（api/provider/feign/feign-provider/api-context）与应用装配规律、新插件/新服务挂载 checklist。触发场景：1) 需要调用其他插件的功能（如 biz 要用 sys 的用户）2) 划分新业务该放哪个模块/哪个服务 3) 新建插件、新的 *-api 接口或新服务。触发词：插件、plugin、模块划分、跨模块、跨插件、api 模块、provider、解耦、依赖、新建插件、跨服务、微服务、Feign、feign、FeignClient、api-context、ContextBean、feign-provider、FeignConstant、装配、新服务、服务拆分。注意：单个业务域内的 CRUD 分层见 crud-development；本技能讲"模块/服务之间怎么组织与通信"。
---

# Snowy 微服务版插件化架构规范

## Maven 模块拓扑

```
snowy-cloud（根 pom，modules：snowy-base / snowy-modules / snowy-plugin / snowy-server）
├── snowy-base/snowy-common              公共基础（人人依赖，含 FeignConstant）
├── snowy-plugin/snowy-plugin-{p}/       插件，每个插件拆成若干子模块：
│   ├── snowy-plugin-{p}-api             接口层（只依赖 snowy-common）
│   ├── snowy-plugin-{p}-func            实现层（依赖本插件 -api + 其他插件的 -api）
│   ├── snowy-plugin-{p}-feign           Feign 接口（依赖本插件 -api）
│   ├── snowy-plugin-{p}-feign-provider  Feign 服务端（依赖本插件 -feign）
│   └── snowy-plugin-{p}-api-context     调用方远程代理（依赖本插件 -feign）
├── snowy-modules/                       启动应用：按需"装配"插件子模块
│   ├── snowy-web-app                    主应用 SnowyWebApp，9101
│   └── snowy-biz-app                    业务应用 SnowyBizApp，9102
└── snowy-server/                        中间件：nacos 8848 / gateway 9003 / actuator 9001 / sentinel 9002 / xxl-job 9004

关键规则：
- 插件实现模块（*-func）之间【禁止】互相依赖
- 插件要调用其他插件 → -func 只依赖对方的 *-api 模块；同服务还是跨服务由启动应用的装配决定，业务代码无感
- 公共代码（工具、注解、基类、服务名常量）下沉到 snowy-common
```

### 五类子模块

| 子模块 | 包 / 写法 | 作用 |
|---|---|---|
| `{p}-api` | `vip.xiaonuo.{p}.api.XxxApi` 接口 | 对外契约，返回 `JSONObject` / `List<JSONObject>` / String / 基本类型 |
| `{p}-func` | `vip.xiaonuo.{p}.{core,modular}`；`modular/{域}/provider/XxxApiProvider`（@Service implements XxxApi） | 真正实现 |
| `{p}-feign` | `vip.xiaonuo.{p}.feign.XxxFeign`，`@FeignClient(name = FeignConstant.WEB_APP 或 BIZ_APP, contextId = "XxxFeign")`，路径 `/feign/{p}/{域}/{方法}` | 远程调用契约，多返回 String(JSON) |
| `{p}-feign-provider` | `vip.xiaonuo.{p}.feign.provider.*`，`@RestController implements XxxFeign` | 被调服务端：转调本地 XxxApi，结果 `JSONUtil.toJsonStr` |
| `{p}-api-context` | `vip.xiaonuo.{p}.api.context.XxxApiContextBean`，`@Component implements XxxApi` | 调用方代理：调 Feign 后 JSONUtil 解析成 JSONObject |

各插件实际拥有的子模块：

| 插件 | -api | -func | -feign | -feign-provider | -api-context |
|---|---|---|---|---|---|
| auth / biz / dev / mobile / sys | ✓ | ✓ | ✓ | ✓ | ✓ |
| client / gen | ✓ | ✓ | — | — | — |
| mocker | -token-api | -token-func | — | — | — |

### 两个应用的装配（提取自 snowy-modules/*/pom.xml）

| 应用 | 本地实现（-func） | 对外提供（-feign-provider） | 远程代理（-feign + -api-context） |
|---|---|---|---|
| snowy-web-app（9101） | auth、client、dev、gen、mobile、sys | auth、dev、mobile、sys | 无 |
| snowy-biz-app（9102） | biz | 无（biz-feign-provider 未装配） | sys、dev、mobile；auth 装的是 -api + -api-context（-feign 经传递依赖带入） |

**装配规律**：同一应用内某插件**要么**装本地实现（-func；需要被别的服务调用再加 -feign-provider），**要么**装远程代理（-feign + -api-context），二者不能同装——本地 provider 与 ContextBean 都实现同一接口，会 Bean 名冲突（如 sys-func 的 `@Service("loginUserApi")` 与 auth-api-context 的 `@Component("loginUserApi")`）。

两启动类都带 `@EnableDiscoveryClient`、`@EnableFeignClients(basePackages = "vip.xiaonuo")`、`scanBasePackages = "vip.xiaonuo"`，所以装进类路径的 Feign 接口与 ContextBean 会被自动扫描。

## 真实调用地图（提取自各插件 -func 的 pom）

A→B = A 的 -func 依赖 B 的 `*-api`：

| 调用方（所在服务） | 依赖的 api | 调用方式 | 典型用途 |
|---|---|---|---|
| **biz**（biz-app，二开主战场） | sys-api、auth-api、dev-api | **跨服务**：api-context → Feign → web-app 的 feign-provider | 用户/登录态（SaBaseLoginUser）/配置文件消息 |
| auth（web-app） | sys-api、dev-api、client-api | 同服务本地注入 | 登录验用户、发验证码、C 端用户 |
| sys（web-app） | auth-api、dev-api、mobile-api | 同服务本地注入 | 登录用户 POJO/在线 token、字典翻译、移动按钮码 |
| dev（web-app） | sys-api、auth-api | 同服务本地注入 | 日志关联用户、监听器类型 |
| mobile（web-app） | auth-api、dev-api、sys-api | 同服务本地注入 | 移动资源引用用户/字典 |
| client（web-app） | auth-api、dev-api | 同服务本地注入 | C 端登录态与工具能力 |
| gen（web-app） | sys-api、mobile-api | 同服务本地注入 | 生成时读模块/菜单、写移动资源 |

- web-app 内部的互调与单体版一样是同 JVM 注入；出厂真正跨服务的只有 **biz-app → web-app** 方向
- 真实链路：biz-app `BizUserServiceImpl` 调 `sysUserApi.ownRole` → `SysUserApiContextBean` → `SysUserFeign`（name=snowy-web-app，`/feign/sys/user/ownRole`）→ web-app `SysUserFeignProvider` → `SysUserApiProvider`
- 反向（web-app → biz-app）：biz 插件已有"机构主管权限转移"的 feign / feign-provider / api-context 三件（`BizOrgDirectorTransferFeign`，name=snowy-biz-app），但**默认未装配**（web-app 没装 biz-api-context，biz-app 没装 biz-feign-provider），使用前请先确认
- 反向通知（被调方 → 调用方）走 `CommonDataChangeEventCenter` 事件，⚠️但它是进程内监听器列表，事件只在发布方所在服务内分发（见 platform-extension 技能）
- gen-api 是空壳；ClientUserApi 在 `vip.xiaonuo.client` 包根（不在 api 子包），且 client 无 feign，只能在 web-app 内本地使用

| 模块 | 职责 | 二次开发可否修改 |
|---|---|---|
| `snowy-base/snowy-common` | CommonResult/CommonException/CommonEntity/工具类/国密/缓存操作器/FeignConstant | 原则上不动 |
| 各插件 `{p}-api` | 跨插件调用接口定义（纯接口 + 少量 POJO/工具） | 可新增接口 |
| `snowy-plugin-sys` 各子模块 | B 端系统：用户/组织/角色/资源/数据范围 | 原则上不动 |
| `snowy-plugin-auth` 各子模块 | 登录鉴权/SSO/三方登录 | 原则上不动 |
| `snowy-plugin-dev` 各子模块 | 开发工具：字典/配置/文件/定时任务/日志/短信/邮件/推送 | 原则上不动 |
| `snowy-plugin-gen` | 代码生成器 | 原则上不动 |
| `snowy-plugin-client` | C 端用户体系 | 原则上不动 |
| `snowy-plugin-mobile` 各子模块 | 移动端资源管理 | 原则上不动 |
| `snowy-plugin-mocker` | 模拟 token（默认未装配） | 原则上不动 |
| **`snowy-plugin-biz`（以 -func 为主）** | **业务插件——二次开发主战场** | ★ 随便写 |
| `snowy-modules/snowy-web-app`、`snowy-biz-app` | 启动 + 全局配置（白名单/异常处理/Feign 拦截器）+ 插件装配 pom | 只改配置类与装配 |
| `snowy-server/*` | 注册配置中心/网关/监控/限流/调度 | 只改配置（网关路由与白名单） |

## 插件内两层结构（-func 模块内）

```
snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz
├── core/                          插件级基础设施（按需存在，biz 出厂只有 config/enums/listener）
│   ├── config/                    插件配置类
│   ├── enums/                     插件级枚举
│   ├── listener/                  数据变更监听（如 BizDataChangeListener）
│   ├── timer/                     定时任务入口（⚠️ biz-app 无 DEV_JOB 调度，见下方缺口）
│   └── util/                      插件级工具
└── modular/                       业务域（一个域一个目录）
    └── {域名}/
        ├── controller/  entity/  enums/  mapper/(+mapping/)
        ├── param/  result/  service/(+impl/)  provider/
```

新业务域默认放 `snowy-plugin-biz-func` 的 `modular/{域名}` 下（参考 notice 域）；只有业务规模大到需要独立插件或独立服务时才新建。

## 跨服务调用五件套（硬性规范）

**场景**：biz-app 的"订单"功能需要根据 userId 拿用户名称。sys 已有 `SysUserApi` 全套五件，**直接注入即可**；下面展示五件各自的写法，新增一个跨服务接口时照此补齐。

**第 1 步**：接口定义放被调方的 -api 模块（先查现成接口再新写）：

```java
// snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api/src/main/java/vip/xiaonuo/sys/api/SysUserApi.java
public interface SysUserApi {

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @author xuyuxiang
     * @date 2022/6/20 18:19
     **/
    JSONObject getUserByIdWithoutException(String userId);
    // ...
}
```

**第 2 步**：被调插件 -func 的 provider 本地实现（@Service）：

```java
// snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/.../modular/user/provider/SysUserApiProvider.java
@Service
public class SysUserApiProvider implements SysUserApi {

    @Resource
    private SysUserService sysUserService;

    @Override
    public JSONObject getUserByIdWithoutException(String userId) {
        SysUser sysUser = sysUserService.getById(userId);
        return JSONUtil.parseObj(sysUser);      // Entity → JSONObject 返回
    }
}
```

**第 3 步**：Feign 接口放 -feign 模块（name 用 FeignConstant，路径 `/feign/{插件}/{域}/{方法}`）：

```java
// snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-feign/src/main/java/vip/xiaonuo/sys/feign/SysUserFeign.java
@FeignClient(name = FeignConstant.WEB_APP, contextId = "SysUserFeign")
public interface SysUserFeign {

    @RequestMapping("/feign/sys/user/getUserByIdWithoutException")
    String getUserByIdWithoutException(@RequestParam(value = "userId", required = false) String userId);
}
```

**第 4 步**：Feign 服务端放 -feign-provider 模块（装在被调方应用里）：

```java
// snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-feign-provider/.../sys/feign/provider/user/SysUserFeignProvider.java
@RestController
public class SysUserFeignProvider implements SysUserFeign {

    @Resource
    private SysUserApi sysUserApi;              // 注入的是本服务内的 SysUserApiProvider

    @Override
    @RequestMapping("/feign/sys/user/getUserByIdWithoutException")
    public String getUserByIdWithoutException(@RequestParam(value = "userId", required = false) String userId) {
        return JSONUtil.toJsonStr(sysUserApi.getUserByIdWithoutException(userId));
    }
}
```

**第 5 步**：调用方代理放 -api-context 模块（装在调用方应用里）：

```java
// snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api-context/.../sys/api/context/SysUserApiContextBean.java
@Component
public class SysUserApiContextBean implements SysUserApi {

    @Resource
    private SysUserFeign sysUserFeign;

    @Override
    public JSONObject getUserByIdWithoutException(String userId) {
        return JSONUtil.parseObj(sysUserFeign.getUserByIdWithoutException(userId));
    }
}
```

> 出厂 feign-provider / api-context 大多用 `@RequiredArgsConstructor` + final 字段（官方历史写法）；二开新写的类按本项目规范用 `@Resource`。

**第 6 步**：调用方业务代码只依赖 -api、只注入接口：

```java
// snowy-plugin-biz-func 的 pom.xml 只依赖 snowy-plugin-sys-api；
// snowy-biz-app 的 pom.xml 装配 snowy-plugin-sys-feign + snowy-plugin-sys-api-context

@Service
public class BizOrderServiceImpl ... {

    @Resource
    private SysUserApi sysUserApi;              // biz-app 里实际注入的是 SysUserApiContextBean

    public void xxx(String userId) {
        JSONObject user = sysUserApi.getUserByIdWithoutException(userId);
        String name = user.getStr("name");
    }
}
```

### 为什么返回 JSONObject 而不是实体类

被调方的 Entity 类在 -func 模块里，调用方不能依赖它（否则插件耦合，且跨服务时调用方类路径上根本没有）。JSONObject（hutool）是双方都能见的 neutral 类型；跨服务时 Feign 层再以 JSON 字符串传输。取字段用 `jsonObject.getStr("name")` / `getInt("age")`。

### 跨服务调用的运行时约定

- 服务名常量只有 `FeignConstant.WEB_APP = "snowy-web-app"`、`FeignConstant.BIZ_APP = "snowy-biz-app"`（`snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/FeignConstant.java`）
- 两个应用各有一个 `FeignRequestInterceptor`（`web/core/interceptor`、`biz/core/interceptor`）：透传当前请求的全部请求头（含 token）+ traceId，被调方因此拿到同一登录态
- 无 Feign fallback、无 Seata：远程失败直接抛异常；`@Transactional` 只管本服务本地事务，跨服务写操作无分布式事务
- 无用户上下文的线程（定时任务、消息消费等）调需登录的 Feign 时，可考虑 mocker-token 插件的 `@CommonMockerToken`——默认未装配到任何应用，使用前请先确认

## 现成 API 速查（先查再用，不要重复造）

| api 模块 | 内容 | biz-app 里能否注入（是否有 ContextBean） |
|---|---|---|
| `snowy-plugin-sys-api` | SysUserApi/SysRoleApi/SysOrgApi/SysPositionApi/SysGroupApi/SysRelationApi/SysTransferApi/SysApi；SysMenuApi/SysButtonApi/SysModuleApi | 前 8 个有；**Menu/Button/Module 无** |
| `snowy-plugin-dev-api` | DevConfigApi/DevFileApi/DevMessageApi/DevSmsApi/DevEmailApi/DevSlideshowApi/DevApi；DevDictApi/DevLogApi/DevJobApi/DevPushApi/DevWeakPasswordApi | 前 7 个有；**Dict/Log/Job/Push/WeakPassword 无** |
| `snowy-plugin-auth-api` | AuthApi、AuthInnerApi、SaBaseLoginUserApi（Bean 名 `loginUserApi` / `clientLoginUserApi`）、SaBaseLoginUser 等 POJO、StpLoginUserUtil 等工具 | 有 |
| `snowy-plugin-mobile-api` | MobileApi/MobileMenuApi/MobileModuleApi/MobileButtonApi | 有 |
| `snowy-plugin-biz-api` | BizUserApi/BizOrgApi/BizPositionApi/BizGroupApi/BizNoticeApi/BizOrgDirectorTransferApi（biz 插件提供） | biz-app 内本地实现 |
| `snowy-plugin-client-api` | ClientUserApi（C 端用户） | 无（client 无 feign，只在 web-app 本地可用） |

web-app 里各插件都是本地实现，上表接口全部可直接注入。
命名惯例：`getXxxByIdWithoutException`（查不到返 null）/ `getXxxByIdWithException`（查不到抛异常）/ `getXxxListByIdList...`（批量）。

## 新插件 / 新服务挂载 checklist

以下步骤按现有装配方式归纳（未逐项实测），落地前请对照 biz 插件与 snowy-biz-app 的现有写法确认。

### A. 新业务插件挂到已有服务

1. 建 `snowy-plugin/snowy-plugin-{name}/` 聚合 pom + 子模块：至少 -api、-func；要被其他服务调用再加 -feign、-feign-provider、-api-context；包 `vip.xiaonuo.{name}`
2. `snowy-plugin/pom.xml` 的 modules 登记；根 `pom.xml` 的 dependencyManagement 登记各子模块
3. 目标应用 pom（如 `snowy-modules/snowy-biz-app/pom.xml`）加 -func；被别的服务调用时本应用再加 -feign-provider，调用方应用加 -feign + -api-context（遵守"不能同装"规律）
4. 接口文档：该应用 Nacos 配置（如 `snowy-biz-app-dev.yaml`）的 `springdoc.group-configs` 加分组；网关 Nacos 配置 `snowy-gateway-app-dev.yaml` 的 knife4j 聚合 routes 加对应分组
5. URL 一级前缀若是新的（不是 /biz/ 等已有前缀）：前端 `snowy-admin-web/src/utils/apiAdaptive.js` 加映射（文件里 `bizCustomization` 为预留示例），并确认网关路由能转发到目标服务，否则经网关 404
6. 免登录接口：网关 `GatewayConfigure`（带 /api/xxx 前缀）+ 目标服务 `GlobalConfigure`（不带前缀）两处都加
7. 前端加 `src/api/{name}/` 与 `src/views/{name}/` 目录

### B. 新建应用（服务）另需

1. `snowy-modules` 下新建应用模块并在 `snowy-modules/pom.xml` 登记；参照 snowy-biz-app：启动类带 `@EnableDiscoveryClient`、`@EnableFeignClients(basePackages = "vip.xiaonuo")`、`scanBasePackages = "vip.xiaonuo"`，并有 `core/config`（GlobalConfigure/AuthConfigure/Knife4jConfigure 等）、`core/interceptor/FeignRequestInterceptor`、`core/handler` 全局异常
2. `FeignConstant` 加新服务名常量
3. Nacos 新增配置 `snowy-xxx-app-dev.yaml`（仓库源文件放 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/`，并同步到 Nacos）；本地 `application.yml` 照 biz-app 只做 Nacos 接入
4. 网关 Nacos 配置加路由 `/api/xxxapp/** → lb://snowy-xxx-app`（StripPrefix=2）与 Knife4j 聚合路由；`GatewayConfigure` 白名单按新前缀补
5. 前端 `apiAdaptive.js` 加前缀映射

⚠️ 大多数情况**不需要**新建插件或服务——在 `snowy-plugin-biz-func` 的 `modular/` 下加业务域即可（`/biz/` 前缀已在网关、apiAdaptive.js、Knife4j 全链路映射到 biz-app）。

## 已知缺口提醒（现状，使用前请先确认）

- **ContextBean 缺口**：dev-api-context 只有 File/Dev/Config/Message/Slideshow/Sms/Email 7 个；sys-api-context 缺 Menu/Button/Module。按现有装配方式，在 biz-app 注入缺失的 Api 会缺 Bean。其中 DevDictFeign 已有 feign-provider；DevLog/DevJob/DevPush/DevWeakPassword 只有 Feign 接口、没有 feign-provider，补齐时两头都要写
- **/feign/\*\* 安全风险（推导未验证）**：两服务 GlobalConfigure 把 `/feign/**` 列为免登录，feign-provider 无 `@SaCheckPermission`，网关未拦截 `/api/*/feign/**`，登录用户可能经网关直调内部接口
- **biz 反向调用未装配**：BizOrgDirectorTransfer 的 feign 三件已写好但未装进任何应用
- **定时任务**：dev-func 只装在 web-app，`CommonTimerTaskRunner` 实现类必须在 web-app 类路径上；biz-func 里写的 Runner 会报"找不到对应的类"；xxl-job 只有调度中心，业务服务未接入执行器
- **数据变更事件不跨服务**：`CommonDataChangeEventCenter` 是进程内列表，web-app 发的事件 biz-app 收不到
- **代码生成器**只生成到 -func 与前端，不生成 feign / api-context，也不改网关与 apiAdaptive.js

## 禁止事项

- ❌ 插件 pom 互相依赖实现模块（如 biz-func 依赖 snowy-plugin-sys-func）
- ❌ 绕过 *-api 直接 import 其他插件 modular 下的类
- ❌ 跨插件返回 Entity/自定义 DTO（会引入类依赖），一律 JSONObject（Feign 层传 JSON 字符串）
- ❌ 把业务代码写进 sys/auth/dev 等平台插件（升级会冲突）
- ❌ 同一应用里同时装某插件的 -func 与 -api-context（Bean 名冲突）
- ❌ 业务代码直接注入 `XxxFeign`、手写 HTTP/RestTemplate 调其他服务

## 检查清单

- [ ] 跨插件调用走了 *-api 接口 + provider 实现
- [ ] 跨服务时五件套齐全：被调方应用装了 -func + -feign-provider，调用方应用装了 -feign + -api-context
- [ ] 返回值 JSONObject，字段名与 Entity 属性名一致
- [ ] 调用方 -func 的 pom 只依赖 *-api 模块
- [ ] 新业务域放对了位置（默认 snowy-plugin-biz-func 的 biz/modular）
- [ ] 先查过现成 api，以及 biz-app 是否有对应 ContextBean，再决定新写
- [ ] 新 URL 前缀 / 新服务已同步：网关路由、Knife4j 聚合、apiAdaptive.js、两处白名单

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api/src/main/java/vip/xiaonuo/sys/api/SysUserApi.java` | 接口定义范本 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/provider/SysUserApiProvider.java` | provider 实现范本（登录时权限码组装在同目录 SysLoginUserApiProvider） |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-feign/src/main/java/vip/xiaonuo/sys/feign/SysUserFeign.java` | Feign 接口范本 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-feign-provider/src/main/java/vip/xiaonuo/sys/feign/provider/user/SysUserFeignProvider.java` | Feign 服务端范本 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api-context/src/main/java/vip/xiaonuo/sys/api/context/SysUserApiContextBean.java` | 远程代理范本 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/FeignConstant.java` | 服务名常量 |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/interceptor/FeignRequestInterceptor.java` | Feign 请求头 + traceId 透传 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/` | dev-api 全家（Config/Dict/File/Sms/Email/Message/Job/Log/Push...） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/core/` | 插件 core 层组织范本 |
| 根 `pom.xml` + `snowy-plugin/pom.xml` + `snowy-modules/snowy-web-app/pom.xml`、`snowy-modules/snowy-biz-app/pom.xml` | 模块聚合与应用装配 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-gateway-app-dev.yaml` | 网关路由与 Knife4j 聚合路由 |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端 URL 前缀 → 网关服务前缀映射 |
