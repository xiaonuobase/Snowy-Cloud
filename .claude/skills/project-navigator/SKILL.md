---
name: project-navigator
description: Snowy 微服务版项目导航地图：目录结构速查、"X 功能在哪个文件"速查表、服务与端口、常用查找命令。触发场景：1) 找某功能/配置/类在哪个文件 2) 不熟悉项目结构想快速定位 3) 新会话了解项目。触发词：在哪、哪个文件、目录结构、导航、找、定位、项目结构、入口、启动类、端口、服务、网关路由、Nacos 配置、Feign 在哪。
---

# Snowy 微服务版项目导航地图

## 顶层结构（30 秒版）

```
snowy-cloud/
├── snowy-base/snowy-common/    公共基础（CommonResult/异常/实体基类/缓存/国密/工具/FeignConstant）
├── snowy-plugin/               8 个插件：sys(系统) auth(鉴权) dev(工具) gen(生成器) client(C端) mobile(移动) mocker(模拟token) biz(★业务)
│                               每个插件拆子模块：-api / -func / -feign / -feign-provider / -api-context（client/gen 只有 -api、-func）
├── snowy-modules/              启动应用：snowy-web-app（9101，平台插件实现）、snowy-biz-app（9102，biz 实现）
├── snowy-server/               中间件：nacos(8848) gateway(9003) actuator(9001) sentinel(9002) xxl-job(9004)
├── snowy-admin-web/            前端（Vue3 + AntdV，JS，端口 9000，请求经网关 9003）
└── .claude/                    本工程化配置
```

插件 -func 内：`{plugin}.core.{config,enums,listener,timer,util}` + `{plugin}.modular.{业务域}.{controller,entity,enums,mapper(+mapping),param,result,service(+impl),provider}`

## 服务与端口速查

| 应用 | 启动类 | 端口 | 说明 |
|---|---|---|---|
| snowy-nacos-app | `com.alibaba.nacos.SnowyNacosApp` | 8848 | 注册/配置中心，控制台 /nacos（nacos/nacos） |
| snowy-actuator-app | `vip.xiaonuo.actuator.SnowyActuatorApp` | 9001 | Spring Boot Admin 监控 |
| snowy-sentinel-app | `com.alibaba.csp.sentinel.dashboard.SnowySentinelApp` | 9002 | Sentinel 控制台（sentinel/sentinel） |
| snowy-gateway-app | `vip.xiaonuo.gateway.SnowyGatewayApp` | 9003 | 网关 + Knife4j 聚合 /doc.html（snowyAdmin/Snowy@2026!） |
| snowy-xxl-job-app | `com.xxl.job.admin.SnowyXxlJobApp` | 9004 | xxl-job 调度中心 /xxl-job-admin |
| snowy-web-app | `vip.xiaonuo.web.SnowyWebApp` | 9101 | 主应用：sys/auth/dev/gen/client/mobile |
| snowy-biz-app | `vip.xiaonuo.biz.SnowyBizApp` | 9102 | 业务应用：biz |

## "我想找 X 在哪" 速查表

| 想找什么 | 位置 |
|---|---|
| 启动类 | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/SnowyWebApp.java`（9101）、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/SnowyBizApp.java`（9102）、`snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/SnowyGatewayApp.java`（9003）、`snowy-server/snowy-nacos-app/src/main/java/com/alibaba/nacos/SnowyNacosApp.java`（8848） |
| 本地配置（只做 Nacos 接入） | `snowy-modules/snowy-web-app/src/main/resources/application.yml`、`snowy-modules/snowy-biz-app/src/main/resources/application.yml` |
| 全部环境配置（数据库/Redis/密钥/端口，Nacos 配置源文件） | `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/snowy-{web,biz,gateway,...}-app-{环境}.yaml`（运行时以 Nacos 控制台为准） |
| Nacos 初始化 SQL | `snowy-server/snowy-nacos-app/src/main/resources/META-INF/nacos-db.sql`（建表 + 全部配置，新装只导它）、`nacos-db-snowy-config-init.sql`（同一批配置记录，仅用于刷新已有库，与前者主键相同，见 env-setup 技能） |
| 建库脚本（33 表） | `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql` |
| xxl-job 建库脚本 | `snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql` |
| 网关路由（/api/webapp、/api/bizapp）与 Knife4j 聚合 | `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-gateway-app-dev.yaml` |
| 网关鉴权白名单（带前缀） | `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` |
| 服务路由白名单（免登录/C端/超管，不带前缀） | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/GlobalConfigure.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/GlobalConfigure.java` |
| 全局异常处理 | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/handler/GlobalExceptionHandler.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/handler/GlobalExceptionHandler.java`；网关：`snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/core/util/GlobalExceptionUtil.java`、`AuthExceptionUtil.java` |
| Feign 请求头透传拦截器 | `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/interceptor/FeignRequestInterceptor.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/interceptor/FeignRequestInterceptor.java` |
| Feign 服务名常量 | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/FeignConstant.java` |
| 统一返回/异常 | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonResult.java`、`exception/CommonException.java` |
| 实体基类（审计字段/逻辑删除） | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonEntity.java` |
| 国密工具/字段加密 | `snowy-base/snowy-common/.../util/CommonCryptogramUtil.java`、`handler/CommonSm4CbcTypeHandler.java` |
| 缓存操作器 | `snowy-base/snowy-common/.../cache/CommonCacheOperator.java` |
| 定时任务接口 | `snowy-base/snowy-common/.../timer/CommonTimerTaskRunner.java`（实现类须在 web-app 类路径上） |
| 数据变更事件中心/监听接口 | `snowy-base/snowy-common/.../listener/CommonDataChangeEventCenter.java`、`CommonDataChangeListener.java` |
| B 端登录流程 | `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/` |
| 登录用户工具（StpLoginUserUtil 等） | `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-api/src/main/java/vip/xiaonuo/auth/core/util/` |
| 权限码组装（登录时） | `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/provider/SysLoginUserApiProvider.java` |
| 用户/角色/组织/菜单管理 | `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/{user,role,org,resource}/` |
| 字典（系统/业务） | `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/.../dev/modular/dict/`、`snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/.../biz/modular/dict/` |
| 文件上传 | `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/.../dev/modular/file/`（装在 web-app；biz-app 经 DevFileApi 远程调用） |
| 短信/邮件/站内信/推送 | `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/.../dev/modular/{sms,email,message,push}/` |
| 定时任务管理 | `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/.../dev/modular/job/` |
| 操作日志切面 | `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/.../dev/core/aop/DevLogAop.java`（只在 web-app 生效） |
| 代码生成器逻辑/模板 | `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/.../gen/modular/basic/`、`snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/` |
| 跨插件接口定义 | `snowy-plugin/snowy-plugin-{p}/snowy-plugin-{p}-api/src/main/java/vip/xiaonuo/{p}/api/*.java`（ClientUserApi 在 `vip/xiaonuo/client/` 包根） |
| 跨服务 Feign 接口 / 服务端 / 远程代理 | `snowy-plugin/snowy-plugin-{p}/snowy-plugin-{p}-feign/.../{p}/feign/`、`-feign-provider/.../{p}/feign/provider/`、`-api-context/.../{p}/api/context/` |
| 应用装配了哪些插件子模块 | `snowy-modules/snowy-web-app/pom.xml`、`snowy-modules/snowy-biz-app/pom.xml` |
| ★ 业务代码（二开主战场） | `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/{域}/` |
| 日志文件 | `logs/{应用名}-log/`（相对启动工作目录，如 `logs/snowy-web-app-log/`） |
| 前端 API 封装 | `snowy-admin-web/src/api/{插件}/xxxApi.js` |
| 前端网关前缀映射 | `snowy-admin-web/src/utils/apiAdaptive.js`（开关 `src/config/index.js` 的 `CLOUD_SERVER`） |
| 前端后端地址/端口 | `snowy-admin-web/.env.development`、`.env.production`（`VITE_API_BASEURL=http://localhost:9003`、`VITE_PORT=9000`）、`snowy-admin-web/vite.config.mjs` |
| 前端页面 | `snowy-admin-web/src/views/{插件}/{域}/index.vue + form.vue` |
| 前端请求封装/工具 | `snowy-admin-web/src/utils/request.js`、`tool.js` |
| 前端组件（38 个组件目录，含 24 个 Xn*） | `snowy-admin-web/src/components/` |
| 前端路由/菜单 | `snowy-admin-web/src/router/`（动态部分来自 SYS_RESOURCE） |
| 前端状态（Pinia） | `snowy-admin-web/src/store/` |
| 前端国际化 | `snowy-admin-web/src/locales/` |
| 前端国密 | `snowy-admin-web/src/utils/smCrypto.js` |

## 常用查找命令

```bash
# 按类名找文件
Glob pattern: **/BizNotice*.java

# 按 URL 找接口（服务内路径，不带网关前缀）
Grep pattern: "/biz/notice/page"  glob: **/*.java

# 按 Feign 路径找跨服务接口
Grep pattern: "/feign/sys/user"  glob: **/*.java

# 查 biz-app 能注入哪些平台 Api（看有哪些远程代理）
Glob pattern: snowy-plugin/**/*-api-context/**/*ApiContextBean.java

# 按注解找（如所有写接口）
Grep pattern: "@CommonLog"  glob: **/controller/*.java

# 按表名找实体
Grep pattern: "@TableName.*BIZ_NOTICE"  glob: **/*.java

# 前端找按钮权限码
Grep pattern: "hasPerm"  path: snowy-admin-web/src/views/biz

# 找网关路由 / Knife4j 聚合分组
Grep pattern: "Path=/api|v3/api-docs"  path: snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev

# 找某功能的菜单 SQL
Grep pattern: "INSERT INTO .SYS_RESOURCE"  path: snowy-modules/snowy-web-app/src/main/resources/_sql
```

## 典型链路（一个请求的旅程）

```
前端 xxxApi.js（baseRequest '/biz/xxx/page'）
  → apiAdaptive.js 加前缀 → request.js 拼成 http://localhost:9003/api/bizapp/biz/xxx/page
  → 网关 9003：SaReactorFilter（GatewayConfigure 白名单 / 登录校验 / 超管路径校验，路径带前缀）
  → 路由 /api/bizapp/** → lb://snowy-biz-app（Nacos 服务发现，StripPrefix=2 → /biz/xxx/page）
  → biz-app 9102：GlobalConfigure 的 SaServletFilter（白名单校验，路径不带前缀）
  → SaInterceptor（@SaCheckPermission URL 校验，权限取自 Redis 中的 loginUser 会话）
  → XxxController.page（@Validated）
  → XxxServiceImpl.page（QueryWrapper.checkSqlInjection + CommonPageRequest）
  → XxxMapper（MP BaseMapper）
  → MySQL snowy-cloud 库（BIZ_XXX，逻辑删除自动过滤）
  → 如需平台数据：XxxApi → XxxApiContextBean → XxxFeign → web-app 9101 的 /feign/** → XxxFeignProvider → XxxApiProvider
  → CommonResult.data(page) 返回
  → request.js 统一处理（code!==200 报错）
```

## 检查清单（导航技能没有代码改动，用于定位）

- [ ] 定位到目标文件后再动手，不要凭记忆猜路径（尤其别套用单体版的 `snowy-web-app/...`、`snowy-plugin-api/...` 路径）
- [ ] 改动前 Read 真实文件（文档可能滞后于代码）
- [ ] 改配置前确认改的是 Nacos 配置（仓库源文件 + Nacos 控制台），不是本地 application.yml
