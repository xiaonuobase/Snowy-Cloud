# CLAUDE.md

本文件是 Snowy 微服务版二次开发项目的工程宪法。所有 AI 辅助开发必须遵守此处规范；详细规范见 `.claude/skills/` 与 `.claude/docs/`。

## 这是什么项目

**Snowy-Cloud v3.0.0（Snowy 微服务版）** —— 小诺开源的国密前后端分离快速开发平台的 Spring Cloud 微服务版本，本项目以它为底座做二次开发。

- 后端：Java 17 + Spring Boot 3.5.16 + Spring Cloud 2025.0.0 + Spring Cloud Alibaba 2023.0.3.3（Nacos 注册/配置中心、Sentinel）+ Spring Cloud Gateway + OpenFeign + MyBatis-Plus 3.5.9 + Sa-Token 1.44.0 + Redisson 3.46.0 + Hutool 5.8.25（**插件化 Maven 多模块 + 多服务架构**）
- 前端：`snowy-admin-web/`（Vue 3.5 + Vite 6 + **Ant Design Vue 4.2.6** + Pinia + vue-i18n，**JavaScript，不是 TS**，包管理用 npm），请求统一经网关（9003）
- 数据库：MySQL 业务库 `snowy-cloud`（脚本 `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql`）+ Nacos 存储库 `snowy-nacos-app-open`，表名**全大写下划线**
- 国密：登录密码 SM2 加密传输、口令 SM3 摘要存储、敏感字段 SM4-CBC 落库加密
- 本项目定位：**二次开发底座**——业务代码写在 `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func`（运行在 `snowy-biz-app` 服务），平台插件（sys/auth/dev/gen/client/mobile）原则上不动

## 目录结构

```
snowy-cloud/
├── snowy-base/
│   └── snowy-common/        公共基础模块（包 vip.xiaonuo.common）：CommonResult/CommonException/
│                            CommonEntity/CommonPageRequest/CommonCacheOperator/国密工具/FeignConstant 等
├── snowy-plugin/            8 个插件（包 vip.xiaonuo.{插件名}），每个插件再拆子模块（见下表）
│   ├── snowy-plugin-sys     B端系统：用户/组织/职位/角色/资源菜单/关系/数据范围
│   ├── snowy-plugin-auth    登录鉴权：B/C端登录、SSO、OAuth2/OIDC/CAS/SAML、三方登录
│   ├── snowy-plugin-dev     开发工具：配置/字典/邮件/文件/定时任务/日志/站内信/监控/短信/推送
│   ├── snowy-plugin-gen     代码生成器（Beetl 模板）
│   ├── snowy-plugin-client  C端功能（CLIENT_USER 独立用户体系）
│   ├── snowy-plugin-mobile  移动端资源管理
│   ├── snowy-plugin-mocker  模拟 token 插件（@CommonMockerToken，默认未装配到任何应用）
│   └── snowy-plugin-biz     ★ 业务插件——二次开发代码放 snowy-plugin-biz-func（biz/modular/{业务域}）
├── snowy-modules/           业务应用（Spring Boot 启动模块）
│   ├── snowy-web-app        主应用：vip.xiaonuo.web.SnowyWebApp，端口 9101，装 sys/auth/dev/gen/client/mobile 的实现
│   └── snowy-biz-app        业务应用：vip.xiaonuo.biz.SnowyBizApp，端口 9102，装 biz 实现 + 平台能力的远程代理
├── snowy-server/            中间件应用
│   ├── snowy-nacos-app      注册/配置中心 8848（Nacos 配置源文件在 META-INF/config-data/{dev,test,prod}/）
│   ├── snowy-gateway-app    网关 9003：路由转发、统一鉴权（SaReactorFilter）、Knife4j 聚合
│   ├── snowy-actuator-app   Spring Boot Admin 监控 9001
│   ├── snowy-sentinel-app   Sentinel 控制台 9002（仅网关接入）
│   └── snowy-xxl-job-app    xxl-job 调度中心 9004（业务服务未接入执行器）
├── snowy-admin-web/         前端工程（独立 npm 项目）
└── .claude/                 本工程化配置（见下文）
```

插件子模块（auth/biz/dev/mobile/sys 五件齐全；client/gen 只有 -api、-func；mocker 为 -token-api、-token-func）：

| 子模块 | 内容 |
|---|---|
| `{p}-api` | 对外接口 `vip.xiaonuo.{p}.api.XxxApi`（只依赖 snowy-common） |
| `{p}-func` | 真正实现 `vip.xiaonuo.{p}.{core,modular}`，含 `provider/XxxApiProvider` |
| `{p}-feign` | Feign 接口 `vip.xiaonuo.{p}.feign.XxxFeign` |
| `{p}-feign-provider` | Feign 服务端 `vip.xiaonuo.{p}.feign.provider.*`（@RestController 转调本地 XxxApi） |
| `{p}-api-context` | 调用方远程代理 `vip.xiaonuo.{p}.api.context.XxxApiContextBean`（@Component implements XxxApi） |

插件内两层结构（-func 模块内，以 biz 为例）：
- `vip.xiaonuo.biz.core.{config,enums,listener,timer,util}` —— 插件级基础设施（按需存在，biz 出厂只有 config/enums/listener）
- `vip.xiaonuo.biz.modular.{业务域}.{controller,entity,enums,mapper,mapper/mapping,param,result,service,service/impl,provider}` —— 业务域标准结构

## 常用命令

```bash
# 后端（JDK 17）首次准备：
#   1) MySQL 建库 snowy-nacos-app-open，导入 snowy-server/snowy-nacos-app/src/main/resources/META-INF/nacos-db.sql
#      （建表 + 全部 18 个服务配置与 3 个命名空间，内容与 META-INF/config-data 一致；新装只导这一个即可）。
#      同目录 nacos-db-snowy-config-init.sql 是同一批配置记录（不含建表），仅用于刷新已有 Nacos 库：两脚本主键相同，需先删同 id 记录
#   2) MySQL 建库 snowy-cloud，导入 snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql（脚本不含 CREATE DATABASE）
#   3) 启动本地 Redis（127.0.0.1:6379，默认用 db 1）
#   4)（可选，启动 xxl-job 时）导入 snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql（自建 snowy-cloud-job 库）
mvn clean install -DskipTests          # 根目录构建
# 运行（IDE 启动各启动类）最少顺序：SnowyNacosApp(8848) → SnowyWebApp(9101)、SnowyBizApp(9102) → SnowyGatewayApp(9003)
#   README 全量顺序：nacos → actuator(9001) → sentinel(9002) → gateway(9003) → xxl-job(9004) → web-app → biz-app
# Nacos 控制台：http://localhost:8848/nacos（nacos/nacos；单机模式、鉴权关闭）
# 接口文档：http://localhost:9003/doc.html（网关聚合 Knife4j，basic 认证 snowyAdmin/Snowy@2026!）
# 出厂登录：superAdmin / Snowy@2026!（⚠️不是 123456；权威来源 DEV_CONFIG 的 SNOWY_SYS_DEFAULT_PASSWORD_FOR_B）
# 登录接口经网关：POST http://localhost:9003/api/webapp/auth/b/doLogin（password 为 SM2 密文）
# 已在用的库密码可能被改过：登录失败不要反复重试，连续输错会锁定账号

# 前端（snowy-admin-web/ 目录下）
npm install
npm run dev                             # 端口 9000，请求直连网关 http://localhost:9003（.env 的 VITE_API_BASEURL）
```

⚠️ 构建环境要求（通用要求，不依赖任何个人电脑的安装路径）：
- JDK 17：与编译目标一致。命令行构建前用 `java -version` 确认是 17；IDEA 里 Project SDK 也选 17
- Maven 3.8+：访问 Maven Central 慢或失败时，在自己的 `~/.m2/settings.xml` 配置阿里云镜像（mirrorOf=central → https://maven.aliyun.com/repository/public），不要改项目 pom
- 部分插件 pom 以 system scope 引用本地 jar（如 snowy-plugin-auth-func），这类 pom 装进本地仓库后会被 Maven 判为无效、传递依赖丢失：只构建单个模块时必须带 `-am`（如 `mvn install -DskipTests -pl snowy-modules/snowy-web-app -am`），或直接在根目录完整构建
- 中间件应用 nacos/sentinel/xxl-job 在各自 pom 里固定了 Spring Boot 2.7.18、java.version 1.8，与主栈（Spring Boot 3.5.16 / Java 17）不同属正常现象，升级主栈时不要连带改它们

## 后端架构

### 分层与 CRUD 标准结构（六件套 + 两可选件）

Controller → Service → Mapper 三层。一个业务域 = `snowy-plugin-biz-func` 下的一个 `modular/{域名}` 目录：

| 件 | 位置 | 要点 |
|---|---|---|
| Entity | `entity/Xxx.java` | `extends CommonEntity`；`@TableName("BIZ_XXX")` 大写；`@TableId private String id`（字符串雪花）；敏感字段加 `typeHandler = CommonSm4CbcTypeHandler.class`（需 `autoResultMap = true`）；字段用 `@Schema(description="中文")` |
| Mapper | `mapper/XxxMapper.java` | `extends BaseMapper<Xxx>`，通常空接口 |
| Mapper XML（可选） | `mapper/mapping/XxxMapper.xml` | 仅自定义 SQL 需要；与 Mapper 同包 |
| Service | `service/XxxService.java` | `extends IService<Xxx>`，方法带中文 Javadoc（@author/@date） |
| ServiceImpl | `service/impl/XxxServiceImpl.java` | `@Service`，`extends ServiceImpl<XxxMapper, Xxx> implements XxxService`；查询 `QueryWrapper<Xxx>().checkSqlInjection()` + lambda 条件；分页 `this.page(CommonPageRequest.defaultPage(), queryWrapper)`；写方法 `@Transactional(rollbackFor = Exception.class)`；业务校验 `throw new CommonException("中文消息{}", 参数)` |
| Controller | `controller/XxxController.java` | `@Tag + @RestController + @Validated`；**URL 直接写方法上**：`@GetMapping("/biz/xxx/page")`（服务内路径，不带网关前缀）；查询 GET + Param 对象、写入 POST + `@RequestBody @Valid`；写操作加 `@CommonLog("中文标题")`；返回 `CommonResult.data(...)` / `CommonResult.ok()` |
| Param 组 | `param/Xxx{Page,Add,Edit,Id}Param.java` | **每个操作一个 Param 类**（不是单个 Bo）；PageParam 含 current/size/sortField/sortOrder/searchKey |
| Result（可选） | `result/XxxResult.java` | 仅投影返回需要，可直接返回 Entity |

URL 规范：`/{插件}/{业务域}/{动作}`，动作动词式（page/add/edit/delete/detail + 业务动作）；查询 GET、写入 POST；delete 收 `List<XxxIdParam>`。
前端经网关访问时自动加服务前缀：`/biz/**` → `/api/bizapp/biz/**`（转 biz-app），`/sys/ /auth/ /dev/ /gen/ /mobile/ /client/` → `/api/webapp/...`（转 web-app），网关 StripPrefix=2 后还原为服务内路径。**新的一级 URL 前缀**：放在已有服务里时只需在前端 `apiAdaptive.js` 登记（否则请求不带服务前缀、经网关 404；网关 `/api/webapp/**`、`/api/bizapp/**` 已覆盖该服务全部路径）；新建服务才另加网关路由；代码包不在已扫描包下时，另加该服务 springdoc 分组与网关 Knife4j 聚合路由（只影响接口文档，不影响访问）。

### 跨插件 / 跨服务调用（硬性规范）

插件之间**禁止**直接依赖对方的 modular 实体类或 -func 模块。业务代码始终**只注入 `XxxApi` 接口**（与单体版写法相同）：同一服务内由对方 provider 本地实现注入；跨服务时由 api-context 远程代理注入。标准五件套：
1. 接口定义放 `{p}-api` 模块（如 `snowy-plugin-sys-api` 的 `SysUserApi`），返回 hutool `JSONObject` / `List<JSONObject>` / String / 基本类型，不返回实体类
2. 被调插件在 `{p}-func` 的 `provider/XxxApiProvider.java`（@Service）实现该接口
3. Feign 接口放 `{p}-feign`：`@FeignClient(name = FeignConstant.WEB_APP 或 BIZ_APP, contextId = "XxxFeign")`，路径 `/feign/{p}/{域}/{方法}`，多返回 String(JSON)
4. Feign 服务端放 `{p}-feign-provider`：`@RestController implements XxxFeign`，转调本地 XxxApi，结果 `JSONUtil.toJsonStr`
5. 调用方代理放 `{p}-api-context`：`@Component implements XxxApi`，内部调 Feign 再 JSONUtil 解析

真实链路：biz-app `BizUserServiceImpl` 调 `sysUserApi.ownRole` → `SysUserApiContextBean` → `SysUserFeign`（name=snowy-web-app，`/feign/sys/user/ownRole`）→ web-app `SysUserFeignProvider` → `SysUserApiProvider`。

装配规律：同一应用内某插件**要么**装本地实现（-func，被别的服务调用再加 -feign-provider），**要么**装远程代理（-feign + -api-context），二者不能同装（Bean 名冲突，如 `loginUserApi`）。服务名常量只有 `FeignConstant.WEB_APP="snowy-web-app"`、`BIZ_APP="snowy-biz-app"`；Feign 拦截器透传全部请求头 + traceId；无 Feign fallback、无 Seata。
详见 `.claude/skills/plugin-architecture`。

### 权限模型（两层，易混淆）

- **后端接口权限**：`@SaCheckPermission("/biz/xxx/page")`，值 = **服务内接口 URL**（不带 `/api/bizapp` 等网关前缀，不是冒号式权限码）；用户拥有的 URL 权限来自角色-资源授权生成的数据范围 apiUrl 列表；biz-app 与网关从 Redis 中 Sa-Token 会话读 loginUser 取权限
- **前端按钮权限**：`hasPerm('bizNoticeAdd')`，驼峰式按钮码，存于 SYS_RESOURCE 表 category=BUTTON 行（code 列），登录时放入 buttonCodeList 下发前端
- 路由级白名单（免登录/C端/超管）**两层都生效**：网关 `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java`（路径**带** `/api/webapp`、`/api/bizapp` 前缀）+ 目标服务的 `GlobalConfigure`（web-app：`snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`；biz-app：`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java`，路径**不带**前缀）；新增免登录接口**两处都要改**
- B 端用 `StpUtil` / `StpLoginUserUtil`；C 端用 `StpClientUtil` / `StpClientLoginUserUtil`（接口前缀 /auth/c/**、/client/c/**，client-func 装在 web-app，经网关为 /api/webapp/...）

## 前端架构（snowy-admin-web）

- API 层：`src/api/{插件}/{xxx}Api.js` —— 模板 `const request = (url, ...arg) => baseRequest('/biz/xxx/' + url, ...arg)`，导出对象方法，第三参为 method（范本 `src/api/biz/bizNoticeApi.js`）
- 网关前缀适配：`src/utils/apiAdaptive.js`（`src/config/index.js` 中 `CLOUD_SERVER: true` 时生效），按 URL 包含的 `/biz/` → `/api/bizapp`、`/sys/ /auth/ /dev/ /gen/ /mobile/ /client/` → `/api/webapp`（取首个命中）；`request.js` 的 baseRequest 拼成 `VITE_API_BASEURL`（http://localhost:9003）+ 前缀 + URL 直连网关
- 页面三件：`src/views/{插件}/{域}/index.vue`（列表 + s-table）+ `form.vue`（弹窗表单，父组件 `formRef.onOpen(record)` 打开）+ 可选 `detail.vue`
- 组件：`src/components/` 下 38 个组件目录（含 24 个 `Xn` 前缀业务组件：XnUpload/XnUserSelector/XnOrgSelector 等，另有 DictSelect/Table(s-table) 等）
- 请求：`src/utils/request.js`（token 头名 `token`、code!==200 统一报错）；C 端用 `clientRequest.js`；国密加密 `src/utils/smCrypto.js`
- 文案走 vue-i18n：`$t('xxx.yyy')`，语言文件在 `src/locales/`

## 后端必须遵守的规范

| 必须 | 禁止 |
|---|---|
| 包名根 `vip.xiaonuo.*` | `com.*` / `org.*` 等其他包名 |
| 每个 .java 头部 12 行 Apache 2.0 版权声明（含 `Copyright [2022] [https://www.xiaonuo.vip]`） | 删除/省略版权头（AI 生成代码最容易漏） |
| 依赖注入用 `@Resource` | `@Autowired`、构造器注入 |
| `extends ServiceImpl<Mapper, Entity> implements XxxService` | 只 implements 不继承 ServiceImpl |
| 对象转换用 Hutool `BeanUtil.toBean / copyProperties` | MapstructUtils / MapStruct 体系 |
| Lombok 只用 `@Getter @Setter` | `@Data` |
| 返回 `CommonResult<T>`、异常 `CommonException` | `R<T>`、`ServiceException`、`RuntimeException` |
| URL 动词式写方法上 + `@SaCheckPermission` 用 URL | 类级 @RequestMapping、RESTful 路径变量、冒号式权限码 |
| 主键 `String` 雪花；表名/字段全大写 | Long 主键、小写表名 |
| 注释/Javadoc/异常消息/Swagger 描述**全中文** | 英文注释 |
| 类名前缀 = 插件缩写（Biz/Sys/Dev/Gen/Auth/Client/Mobile）；跨服务件按 `XxxFeign` / `XxxFeignProvider` / `XxxApiContextBean` 命名 | 无前缀类名 |
| 查询构造带 `checkSqlInjection()`，写操作带 `@Transactional(rollbackFor = Exception.class)` | 裸 QueryWrapper、无事务写操作 |
| 业务代码只注入 `XxxApi` 接口，跨服务按五件套补齐 feign / feign-provider / api-context | 业务代码直接注入 `XxxFeign`、手写 HTTP 调其他服务、依赖对方 -func 模块 |

注：出厂 feign-provider / api-context 大多用 `@RequiredArgsConstructor` 构造器注入（官方历史写法），二开新写的类仍按 `@Resource`。

## ⚠️ 易错点警告（先读再写代码）

**来自 RuoYi的惯性错误**——本项目的约定与它们**方向相反**：
1. ❌ `implements IXxxService` 不继承 → ✅ 必须 `extends ServiceImpl<XxxMapper, Xxx>`
2. ❌ `@RequiredArgsConstructor` 构造器注入 → ✅ `@Resource` 字段注入
3. ❌ `MapstructUtils.convert()` → ✅ `BeanUtil.toBean()`
4. ❌ `@Data` → ✅ `@Getter @Setter`
5. ❌ RESTful `/list`、`/{id}`、PUT/DELETE → ✅ `/page`、`/detail`、全 POST
6. ❌ 权限码 `system:user:list` → ✅ 权限码 = 接口 URL `/sys/user/page`
7. ❌ 单个 Bo + AddGroup/EditGroup → ✅ 每操作一个 Param 类
8. ❌ Long 雪花主键 → ✅ String 主键；❌ 小写表名 → ✅ 全大写

**来自 Snowy 单体版的惯性错误**——微服务版路径与运行方式不同：
1. ❌ 启动 `vip.xiaonuo.Application`、端口 82、前端 81 → ✅ SnowyWebApp(9101) / SnowyBizApp(9102)，前端 9000 经网关 9003
2. ❌ 改 `snowy-web-app/src/main/resources/application.properties` → ✅ 配置在 Nacos（dataId 如 `snowy-web-app-dev.yaml`，namespace `snowy-cloud-dev`、group `dev`）；仓库源文件在 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/`；本地 `application.yml` 只做 Nacos 接入
3. ❌ 业务代码写进 `snowy-plugin/snowy-plugin-biz/src/...` → ✅ `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/...`
4. ❌ 接口定义找 `snowy-plugin-api/` 目录 → ✅ 在各插件内的 `{p}-api` 子模块（如 `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api`）
5. ❌ 白名单只改服务的 GlobalConfigure → ✅ 网关 GatewayConfigure（带前缀）+ 服务 GlobalConfigure（不带前缀）两处
6. ❌ 以为 biz-app 里任何平台 Api 都能注入 → ✅ 先确认 biz-app 装了对应 api-context Bean（见下方"现状"）
7. ❌ 新增一级 URL 前缀只写后端 → ✅ 至少在 apiAdaptive.js 登记；新建服务另加网关路由；新代码包另加 springdoc 分组与 Knife4j 聚合（只影响文档）
8. ❌ 指望 `@Transactional` 回滚另一个服务的写操作 → ✅ 无 Seata，事务只管本服务本地库操作

其他易错点：
- **版权头**：AI 生成的新 Java 文件必须补 12 行版权声明（pre-tool-use hook 会警告）
- **逻辑删除**：`DELETE_FLAG` 继承自 CommonEntity，查询自动过滤，不要手写 delete_flag 条件
- **对象转换到 Entity 时**：`BeanUtil.toBean(param, Xxx.class)` 用于新增；编辑先 `queryEntity(id)` 查出再 `BeanUtil.copyProperties(param, entity)`
- **新业务表**用 `BIZ_` 前缀（出厂 biz 域复用了 SYS_/DEV_ 表是历史设计，新表不要模仿）
- 前端是 **Ant Design Vue + JS**：不要写 Element Plus 组件、不要写 TypeScript
- **代码生成器**：生成到 `snowy-plugin/{插件}/{插件}-func` 与 `snowy-admin-web/src`，生成器跑在 web-app 且工作目录（user.dir）必须是仓库根目录；不生成 feign/api-context，也不改网关与 apiAdaptive.js（moduleName 不在映射表里时经网关 404）
- **本地 Redis**：多个 Snowy 项目共用同一 Redis 库且 token 名、superAdmin ID 相同时，会读到别的项目的会话导致登录 ClassCastException，本地各项目宜用不同 Redis 库号

⚠️ 现状（原有缺陷/限制，只作提醒，不是推荐做法）：
- **api-context 缺口**：dev-api-context 只有 File/Dev/Config/Message/Slideshow/Sms/Email 7 个 ContextBean，DevDictApi/DevLogApi/DevJobApi/DevPushApi/DevWeakPasswordApi 无；sys-api-context 缺 SysMenuApi/SysButtonApi/SysModuleApi。按现有装配方式，在 biz-app 注入这些 Api 会缺 Bean，使用前请先确认
- **/feign/\*\* 安全风险（推导未验证）**：两服务 GlobalConfigure 把 `/feign/**` 列为免登录，feign-provider 无 `@SaCheckPermission`，网关未拦截 `/api/*/feign/**`，登录用户可能经网关直调内部接口
- **定时任务**：dev-func 只装在 web-app，DEV_JOB 只有 web-app 调度，`CommonTimerTaskRunner` 实现类必须在 web-app 类路径上；写在 biz-func（biz-app）里的 Runner 会报"找不到对应的类"。xxl-job 只有调度中心，业务服务未接入执行器
- **操作日志**：DevLogAop 只在 dev-func（web-app），biz-app 里的 `@CommonLog` 不落操作日志（推导）；锁屏校验也只在 web-app
- **网关 403 变 500**：非超管访问超管接口时，网关 AuthExceptionUtil 的 NotRoleException 分支在响应式环境调 `CommonServletUtil.getRequest()`，返回 500 而非 403
- **数据变更事件不跨服务**：`CommonDataChangeEventCenter` 是进程内静态监听器列表，web-app 里 sys 发的事件到不了 biz-app 的监听器

## 新增业务功能时的流程

1. 设计表（库 `snowy-cloud`；BIZ_ 前缀大写、String 雪花主键、审计字段组齐全，参考 `.claude/skills/database-ops`）
2. 开发：`/dev` 命令（模式 A：AI 直写六件套 + 前端三件；模式 B：用平台自带代码生成器）或 `/crud`（表已存在）；后端落 `snowy-plugin-biz-func` 的 `biz/modular/{域}`，URL 用 `/biz/` 前缀（已映射到 biz-app，无需改网关与 apiAdaptive.js）
3. 菜单 SQL：SYS_RESOURCE 表 INSERT（MENU 行 + BUTTON 行，按钮码驼峰式，参考 gen 插件 sqlend 模板）
4. 重启 snowy-biz-app + 刷新前端，在角色管理里给角色授权新资源
5. `/check` 检查规范；完整度口径见 `.claude/framework-config.json`（六件套 /6）
6. 若新能力要被 web-app 调用、或要新增一级 URL 前缀 / 新服务：按 `.claude/skills/plugin-architecture` 的五件套与挂载步骤补齐

## .claude 目录说明

| 目录 | 内容 |
|---|---|
| `skills/` | 26 个技能（crud-development、code-patterns、security-auth、env-setup、api-verify、platform-extension 等），hook 会强制评估激活 |
| `commands/` | 10 个斜杠命令：/start /dev /crud /check /sync /update-status /progress /next /init-docs /add-todo |
| `agents/` | 2 个子代理：code-reviewer（只读审查）、project-manager（项目管理文档） |
| `docs/` | 8 篇开发指南（框架说明/后端/前端/数据库/工具类/国密安全等） |
| `templates/` | 3 个管理文档模板（需求/项目状态/待办清单） |
| `hooks/` | 3 个 hook：强制技能评估、危险命令拦截、nul 清理 |
| `framework-config.json` | 框架 vs 业务划分与进度统计规则（机器可读） |

进度统计口径：出厂 biz 7 域（index/dict/group/notice/org/position/user）为演示代码**不计进度**，新建业务域才计入（完整度 = 六件套存在数 / 6）；扫描根目录 `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular`。

## 外部依赖说明

- 无 `.mcp.json`（未配置数据库 MCP）；需要查库时用 mysql CLI，不可用时 SQL 落盘 `docs/sql-pending/` 提示手动执行
- 数据库连接从 Nacos 中各服务配置（如 `snowy-web-app-dev.yaml`）的 `spring.datasource.dynamic.datasource.master` 段动态解析；仓库源文件为 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml`，运行时以 Nacos 控制台中的内容为准（可能已被改过），**禁止在任何文档/命令里硬编码连接串**
- 无 `.codegraph` 索引；定位代码优先用 Glob/Grep 或 project-navigator 技能的速查表
- git 仓库已初始化（主分支 master，开发分支 dev）；提交规范见 git-workflow 技能
