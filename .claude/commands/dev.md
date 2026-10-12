---
description: 开发新功能（建表 + 后端六件套 + 前端三件 + 菜单SQL，双模式）
---

# /dev —— 开发新业务功能

按以下流程开发一个新业务功能。**全程遵循已加载的技能规范**（crud-development / database-ops / code-generator / frontend-pc / code-patterns）。

## 第 1 步：需求确认

向用户确认（一次问全，不要挤牙膏）：
1. 功能名称与业务用途（一句话）
2. 核心字段与查询条件（给一个建议清单让用户增删）
3. 页面形态：普通表格 / 树形 / 左树右表 / 主子表
4. 开发模式：
   - **模式 A（AI 直写）**：AI 直接生成全部代码（推荐：可控、规范一致）
   - **模式 B（平台生成器）**：AI 建表并给出 GenBasic 配置清单，用户走平台"开发工具→代码生成"生成，AI 负责生成后的核对修补（适合与官方模板保持极致一致）

## 第 2 步：功能重复检查（强制，不可跳过）

```bash
# Grep 检查是否已有同名/同义业务域
Grep pattern: "(?i)(功能名英文|表名)"  path: snowy-plugin/snowy-plugin-biz/
ls snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/
```
发现重复 → 停止，向用户报告冲突并询问是扩展已有模块还是确认新建。

## 第 3 步：设计表

按 database-ops 技能规范：
- 表名 `BIZ_{域名}` 全大写；`ID varchar(20)` 主键；审计字段组齐全；字段中文 COMMENT
- 输出建表 SQL 给用户确认（此时尚未执行）

## 第 4 步：执行建表（降级链）

建表到业务库 **snowy-cloud**。数据库连接从 Nacos 配置源文件 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-biz-app-dev.yaml` 的 `spring.datasource.dynamic.datasource.master` 段动态解析（运行时以 Nacos 控制台 `http://localhost:8848/nacos` 里的同名配置为准；**禁止硬编码口令**）：
1. mysql CLI 可用 → 直接执行建表
2. 不可用 → SQL 写入 `docs/sql-pending/YYYY-MM-DD-{功能}.sql`，明确提示用户手动执行后再继续第 6 步

## 第 5 步：生成方案确认（仅一次）

列出将生成的文件清单（后端六件套 9-11 个文件 + 前端三件 + 菜单 SQL），用户确认后进入执行。同时确认：该功能是否需要被**其他服务**调用——需要才在清单里追加 biz-api 接口、provider、feign 三件与两个应用 pom 的装配改动（见 crud-development 技能），默认不需要。

## 第 6 步：生成代码

### 模式 A（AI 直写）
1. **先 Read 范本**：`snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/` 下的 Controller/Entity/Service/ServiceImpl/Param（强制，逐文件对照）
2. 按顺序生成到 `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/{域名}/`（全部带 12 行版权头、中文注释、@author/@date）：
   - `entity/Biz{Xxx}.java` → `mapper/Biz{Xxx}Mapper.java` → `service/Biz{Xxx}Service.java` → `service/impl/Biz{Xxx}ServiceImpl.java` → `param/` 四类 → `controller/Biz{Xxx}Controller.java`（→ 需要时 `result/`、`enums/`）
   - Controller 与 @SaCheckPermission 只写 `/biz/{域名}/{动作}`，不带网关前缀
3. 前端三件：`snowy-admin-web/src/api/biz/biz{Xxx}Api.js`（`/biz/{域名}/` 前缀，由 apiAdaptive.js 自动转 `/api/bizapp`；域名不要取 sys/auth/client/dev/gen/mobile）+ `views/biz/{域名}/index.vue` + `form.vue`
4. 菜单 SQL：SYS_RESOURCE 的 MENU 行 + BUTTON 行（驼峰按钮码），按 code-generator 技能里 sqlend 模板格式。**挂载点直接用出厂真实 ID**（见 database-ops 技能"菜单挂载点速查"：MODULE_ID=业务模块 `1548901111999773976`，PARENT_ID=`'0'` 或公司架构目录 `1548901111999773977`），不留占位符

### 模式 B（平台生成器引导）
1. 确认表已建（第 4 步）
2. 输出 GenBasic 配置清单：pluginName=`snowy-plugin-biz`（表单默认值）、moduleName=`biz`、tablePrefix=`Y`、busName、className、module/menuPid（让用户在界面选所属模块与上级菜单）、genType=形态、generateType（ZIP 或 PRO）、authorName
3. 指导用户：启动 nacos/web-app/网关/前端 → 开发工具→代码生成→导入表→填配置→字段配置→生成
4. 用户生成后，AI 执行"生成后必做"：核对落位（biz-func 下）/版权头/URL/权限码；ZIP 方式执行菜单 SQL（PRO 方式生成器已自动插入并授权超管）；提醒授权

## 第 7 步：收尾清单

- [ ] 菜单/按钮 SQL 已执行（或已落盘 sql-pending 并告知）
- [ ] 提醒用户：重启 **snowy-biz-app**（新代码只在它里面加载）→ 前端 `http://localhost:9000` 系统管理→角色管理，给角色"授权资源"（菜单+按钮）与"授权权限"（`/biz/{域名}/*`，手写接口连超管角色也要授）→ 前端刷新
- [ ] **接口自测**（服务已启动时，见 api-verify 技能，全部走网关 9003）：SM2 加密密码登录 superAdmin/Snowy@2026!（`POST http://localhost:9003/api/webapp/auth/b/doLogin`）拿 token → curl `http://localhost:9003/api/bizapp/biz/{域名}/...` 五连（page/add/校验拦截/edit/delete）→ 输出自测报告；返回无权限时先检查超管角色是否已"授权权限"
- [ ] 建议运行 `/check` 审查本次产出
- [ ] 询问是否记入任务跟踪（更新 docs/待办清单.md 或项目状态.md）

## AI 强制规则

1. 版权头 12 行一个文件都不能漏（hook 会警告）
2. 代码风格逐 token 对照 notice 范本，禁止 RuoYi 惯性写法（17 条反向清单与微服务特有禁令见 code-patterns）
3. @SaCheckPermission 值 = URL（不带网关前缀）；按钮码 = 驼峰
4. 表/字段全大写；String 主键
5. 查询条件判空 + checkSqlInjection；写方法事务
6. 前端 JS 非 TS，AntdV 组件，s-table loadData 模式
7. 生成文件前先 Read 对应范本文件
8. 不修改 sys/auth/dev/gen 等 6 个平台插件的代码
9. 建表 SQL 执行前必须经用户确认
10. 完成后主动给出验证步骤（启动 nacos→web-app、biz-app→网关→前端 → 用出厂账号 superAdmin 登录 → 角色授权 → 页面操作 → 网关 Knife4j `http://localhost:9003/doc.html` 测试）
11. 业务代码只写进 snowy-plugin-biz-func；跨服务只注入 XxxApi，不直接注入 Feign、不依赖其他插件的 -func
