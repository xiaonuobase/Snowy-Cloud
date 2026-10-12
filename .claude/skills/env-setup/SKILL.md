---
name: env-setup
description: Snowy 新环境搭建与首次启动 playbook：JDK/MySQL/Redis/前端依赖/建库导入/启动验证全清单，含出厂账号真实密码。触发场景：1) 新机器/新同事把项目跑起来 2) 环境重置后重新初始化 3) 启动前的就绪自检 4) 查出厂登录账号密码。触发词：环境搭建、首次启动、跑起来、初始化、安装依赖、导入数据库、建库、登录不了、默认密码、出厂账号、superAdmin、新环境、就绪检查、Nacos、网关、服务注册、启动顺序、端口。注意：启动后的故障排查见 bug-detective；本技能讲"从零到能登录"。
---

# Snowy 环境搭建与首启

## 就绪清单（按顺序）

| # | 依赖 | 要求                                                                                                    | 验证 |
|---|---|-------------------------------------------------------------------------------------------------------|---|
| 1 | JDK | **17**（主栈编译目标；命令行构建和 IDEA 的 Project SDK 都用 17）。nacos/sentinel/xxl-job 三个中间件应用自带 Spring Boot 2.7.18、编译目标 Java 8，是独立版本线，不要拿主栈依赖版本去改它们 | `java -version` |
| 2 | MySQL · Nacos 库 | 8.0/5.7，建库 `snowy-nacos-app-open`（utf8mb4），导入 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/nacos-db.sql`（建表 + 全部配置，只导这一个；见下方"Nacos 脚本注意"） | `mysql -uroot -p snowy-nacos-app-open -e "SELECT data_id FROM config_info WHERE tenant_id='snowy-cloud-dev'"` 应列出 6 条 |
| 3 | MySQL · 业务库 | 建库 `snowy-cloud`（utf8mb4；脚本不含 CREATE DATABASE，需先手动建），导入 `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql`（33 张表） | `mysql -uroot -p snowy-cloud -e "SELECT COUNT(*) FROM SYS_USER"` 应 ≥2 |
| 4 | MySQL · xxl-job 库（可选） | 只有要启动 snowy-xxl-job-app 时才需要：执行 `snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql`（脚本自建 `snowy-cloud-job` 库） | `mysql -uroot -p -e "SHOW DATABASES LIKE 'snowy-cloud-job'"` |
| 5 | Redis | 本地 6379，无密码，用 database 1；web-app、biz-app、网关三者**必须同库**（Sa-Token 会话靠 Redis 跨服务共享） | `redis-cli -n 1 ping` |
| 6 | 配置核对 | 各服务本地 `application.yml` 只做 Nacos 接入（localhost:8848、命名空间 `snowy-cloud-dev`、分组 `dev`、nacos/nacos）；数据源/Redis 在 Nacos 配置 `snowy-web-app-dev.yaml`、`snowy-biz-app-dev.yaml`、`snowy-gateway-app-dev.yaml`（仓库默认 MySQL root/123456）；Nacos 自身连库在 `snowy-server/snowy-nacos-app/src/main/resources/application.properties`（默认 root/123456）。与自己的数据库不同则改 | Nacos 控制台 `http://localhost:8848/nacos`（nacos/nacos）→ 配置管理 → 命名空间 snowy-cloud-dev |
| 7 | 后端构建启动 | 根目录 `mvn clean install -DskipTests` 后按下方"启动顺序与端口"在 IDEA 中依次启动；最少需要 nacos → web-app、biz-app → gateway | 下方自检命令全部通过 |
| 8 | 前端依赖 | `snowy-admin-web/` 下 `npm install`（Node ≥18）                                                                    | — |
| 9 | 前端启动 | `npm run dev`，端口 **9000**；`.env.development` 的 `VITE_API_BASEURL=http://localhost:9003`，请求直接发往网关，由 `src/utils/apiAdaptive.js` 自动补 `/api/webapp`、`/api/bizapp` 前缀 | 打开 `http://localhost:9000` |
| 10 | 登录验证 | 前端用出厂账号登录看到首页；或按 api-verify 技能直调 `POST http://localhost:9003/api/webapp/auth/b/doLogin` | 返回 token / 进入首页 |

### 启动顺序与端口

| 顺序 | 应用 | 启动类 | 端口 | 是否必需 |
|---|---|---|---|---|
| 1 | snowy-nacos-app | `com.alibaba.nacos.SnowyNacosApp` | 8848（`/nacos`，nacos/nacos，单机、鉴权关） | 必需（注册中心 + 配置中心） |
| 2 | snowy-actuator-app | `vip.xiaonuo.actuator.SnowyActuatorApp` | 9001 | 可选（Spring Boot Admin 监控） |
| 3 | snowy-sentinel-app | `com.alibaba.csp.sentinel.dashboard.SnowySentinelApp` | 9002（sentinel/sentinel） | 可选（只接在网关） |
| 4 | snowy-gateway-app | `vip.xiaonuo.gateway.SnowyGatewayApp` | **9003** | 必需（前端与接口文档都走它） |
| 5 | snowy-xxl-job-app | `com.xxl.job.admin.SnowyXxlJobApp` | 9004（`/xxl-job-admin`，admin/123456） | 可选（业务服务未接入执行器） |
| 6 | snowy-web-app | `vip.xiaonuo.web.SnowyWebApp` | **9101** | 必需（auth/sys/dev/gen/client/mobile） |
| 7 | snowy-biz-app | `vip.xiaonuo.biz.SnowyBizApp` | **9102** | 必需（biz 业务） |

上表是 README 的全量顺序。只想跑通登录与业务：nacos → web-app → biz-app → gateway → 前端。

### Nacos 脚本注意

- `nacos-db.sql` 是整库导出：Nacos 建表 + 18 条配置（dev/test/prod 各 6 个 yaml）+ 3 个命名空间，配置内容与 `config-data` 源文件逐字一致，新装只导它即可；`nacos-db-snowy-config-init.sql` 只含同一批配置与命名空间记录（不含建表）
- ⚠️ 两个脚本的 `config_info`、`tenant_info` 主键相同：在导过 `nacos-db.sql` 的库上再执行 `nacos-db-snowy-config-init.sql` 会报 Duplicate entry 并中断。它只用于给已有 Nacos 库刷新出厂配置——先删除同 id 记录再导入（会覆盖在控制台改过的配置，执行前先备份）
- 配置源文件在仓库 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/`，改动后需同步到 Nacos（控制台编辑或重新导入）才生效

## ★ 出厂登录账号（本仓库种子数据的真实值）

| 账号 | 密码 | 角色 |
|---|---|---|
| `superAdmin` | **`Snowy@2026!`** | 超级管理员（拥有全部权限） |
| `bizAdmin` | **`Snowy@2026!`** | 业务管理员（仅业务模块） |

> ⚠️ **不是 123456**——网上教程/官方演示站说的 123456 是 xiaonuo.vip 演示库的密码。本仓库种子密码的权威来源是 DEV_CONFIG 表 `SNOWY_SYS_DEFAULT_PASSWORD_FOR_B` 配置项（新建用户的默认密码也是它，可在 开发工具→系统配置 里改）。存储为 SM3 摘要：SM3("Snowy@2026!") = b7eb53ce42289cd168e21e37cc3b94333c3e2e691486548a06f5c5fae129f157。
> 登录失败次数过多会锁定（PWD_ERROR 处理逻辑），连不上先确认大小写与感叹号。
> 其他中间件账号：Nacos nacos/nacos、Knife4j（网关聚合）snowyAdmin/Snowy@2026!、Druid 监控页出厂默认关闭（开启后 snowyAdmin/Snowy@2026!）、Sentinel sentinel/sentinel、xxl-job admin/123456。

## ⚠️ 本地多个 Snowy 项目共用同一 Redis 库会串会话

本机同时有单体版、其他 Snowy 项目时，若它们都用 Redis database 1、token 名都是 `token`、superAdmin 的 ID 又相同，就会读到别的项目写入的会话，登录时出现 ClassCastException 等怪错。本地各项目宜用不同 Redis 库号：改 Nacos 里 `snowy-web-app-dev.yaml`、`snowy-biz-app-dev.yaml`、`snowy-gateway-app-dev.yaml` 三份的 `spring.data.redis.database`（Sa-Token 的 alone-redis 引用同一值），**三份必须改成同一个库号**，改完重启三个服务并清掉浏览器里的旧 token。

## 常用自检命令

```bash
# 端口占用（8848 Nacos / 9003 网关 / 9101 web-app / 9102 biz-app / 9000 前端）
# Windows：
netstat -ano | findstr ":8848 :9003 :9101 :9102 :9000"
# macOS / Linux：
lsof -nP -iTCP -sTCP:LISTEN | grep -E ':(8848|9003|9101|9102|9000) '

# Nacos 活着吗（应输出 OK）
curl -s http://localhost:8848/nacos/v1/console/health/readiness

# 服务注册上了吗（dev 命名空间 + dev 分组，doms 里应有 snowy-web-app / snowy-biz-app / snowy-gateway-app）
curl -s "http://localhost:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=20&groupName=dev&namespaceId=snowy-cloud-dev"

# 各服务直连活着吗（应输出 WELCOME）
curl -s http://localhost:9101/
curl -s http://localhost:9102/

# 网关转发通吗（免登录接口，应返回 code 200 的 JSON）
curl -s http://localhost:9003/api/webapp/dev/config/sysBaseList

# 接口文档（网关聚合，basic 认证 snowyAdmin/Snowy@2026!）
curl -s -u 'snowyAdmin:Snowy@2026!' http://localhost:9003/doc.html | head -5

# 表齐了吗（业务库应为 33 张）
mysql -uroot -p snowy-cloud -e "SELECT COUNT(*) AS tables_cnt FROM information_schema.tables WHERE table_schema='snowy-cloud';"
```

Windows 下 `curl` 用系统自带的 curl.exe 即可（PowerShell 里写 `curl.exe`，避免被 Invoke-WebRequest 别名接管）。

## AI 首启引导流程（用户说"跑不起来/帮我搭环境"时）

1. 按就绪清单逐项检查，从失败的那项开始修
2. 常见首启故障（详见 bug-detective 树 A / 树 E）：端口占用 / 库没导 / Nacos 未起或配置没拉到 / 服务未注册 / Redis 没起或与其他项目共库 / JDK 版本 / 口令不符
3. 全部就绪后：打开前端 `http://localhost:9000` → superAdmin / Snowy@2026! 登录 → 看到首页即完成
4. 登录成功后建议用户立刻改密码（系统管理 → 用户中心）

## 新增依赖说明

- Maven 中央仓库直连失败时用阿里云镜像（在自己的 `~/.m2/settings.xml` 配 mirrorOf=central → maven.aliyun.com/repository/public，不改项目 pom）
- npm 失败时用 npmmirror：`npm install --registry=https://registry.npmmirror.com`

## 检查清单

- [ ] 10 项就绪清单全过（xxl-job 库可按需跳过）
- [ ] Nacos 服务列表里 snowy-web-app、snowy-biz-app、snowy-gateway-app 均已注册
- [ ] 登录成功（superAdmin / Snowy@2026!）
- [ ] 提醒用户改默认密码
- [ ] 本机偏离默认的配置（数据库口令、Redis 库号等）已改在 Nacos 配置里，且未把明文口令变更提交进仓库的 config-data / nacos SQL 脚本

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/` | 各服务 Nacos 配置源文件（数据源/Redis/端口/Sa-Token/网关路由/文档认证） |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/nacos-db.sql`、`nacos-db-snowy-config-init.sql` | Nacos 库建表与全部配置（新装用前者）；刷新已有库的配置（后者） |
| `snowy-server/snowy-nacos-app/src/main/resources/application.properties` | Nacos 端口 8848、自身连库配置 |
| `snowy-modules/snowy-web-app/src/main/resources/application.yml` | 服务本地 Nacos 接入（namespace/group/data-id），biz-app、gateway 同结构 |
| `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql` | 业务库种子数据（账号/菜单/DEV_CONFIG 默认密码） |
| `snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql` | xxl-job 库（可选） |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/SnowyWebApp.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/SnowyBizApp.java` | 两个业务服务启动类 |
| `snowy-admin-web/.env.development`、`snowy-admin-web/src/utils/apiAdaptive.js` | 前端端口 9000、网关地址、URL 前缀映射 |
| `snowy-admin-web/package.json` | 前端依赖与脚本 |
| `README.md` 快速启动节 | 官方全量启动顺序与端口表 |
