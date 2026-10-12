---
name: database-ops
description: Snowy 数据库设计规范：大写表名、BIZ_ 前缀、String 雪花主键、审计字段组、建表模板、菜单/按钮 SYS_RESOURCE SQL、SQL 脚本落点、多数据源。触发场景：1) 新功能设计建表 2) 写菜单/按钮资源 SQL 3) 查表结构或写查询 SQL 4) 配置数据源。触发词：数据库、建表、SQL、表设计、菜单SQL、SYS_RESOURCE、字典数据、主键、雪花、DELETE_FLAG、数据源、dynamic、snowy-cloud 库、Nacos 数据源、Nacos 库、xxl-job 库。
---

# Snowy 数据库设计规范

## 命名规范

| 项 | 规则 | 示例 |
|---|---|---|
| 表名 | **全大写下划线**，前缀 = 归属插件 | `BIZ_SUPPLIER`、`SYS_USER`、`DEV_CONFIG` |
| 字段名 | 全大写下划线 | `SUPPLIER_NAME`、`CREATE_TIME` |
| 新业务表前缀 | **`BIZ_`**（出厂 biz 域复用 SYS_/DEV_ 表是历史设计，**新表不要模仿**） | `BIZ_ORDER` |
| 主键 | `ID varchar(20)` 字符串雪花（MyBatis-Plus ASSIGN_ID） | ❌ bigint 自增 |
| 每表必有 | 表 COMMENT + 每字段 COMMENT（**中文**） | — |
| 字符集 | utf8mb4 / utf8mb4_general_ci，InnoDB | — |

## 标准建表模板（复制改业务字段）

```sql
CREATE TABLE `BIZ_XXX`  (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  -- ↓↓↓ 业务字段区（全大写，带中文 COMMENT）↓↓↓
  `NAME` varchar(100) NULL DEFAULT NULL COMMENT '名称',
  `STATUS` varchar(10) NULL DEFAULT NULL COMMENT '状态',
  -- ↓↓↓ 尾部固定字段组（顺序保持一致）↓↓↓
  `SORT_CODE` int(11) NULL DEFAULT NULL COMMENT '排序',
  `REMARK` varchar(500) NULL DEFAULT NULL COMMENT '备注',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL DEFAULT NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL DEFAULT NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'XXX表' ROW_FORMAT = Dynamic;
```

要点：
- 尾部字段组与 `CommonEntity` 一一对应：DELETE_FLAG（逻辑删除，值 NOT_DELETE/DELETED）、CREATE_TIME/CREATE_USER/UPDATE_TIME/UPDATE_USER（自动填充，代码里**不要**手动赋值）、SORT_CODE（默认排序）、REMARK、EXT_JSON（扩展 JSON）
- 树形表加 `PARENT_ID varchar(20) NULL COMMENT '父id'`
- 类型映射：String→varchar(n)/text、Integer→int、BigDecimal→decimal(总长,小数)、日期时间→datetime、大文本→longtext
- 需要加密落库的字段（手机号等）正常建 varchar，加密由 `CommonSm4CbcTypeHandler` 在应用层做（密文更长，长度适当放宽）

## 逻辑删除

- 字段 `DELETE_FLAG`，有效值 `NOT_DELETE` / `DELETED`（见 `CommonDeleteFlagEnum`）
- Entity 继承 CommonEntity 自动带 @TableLogic，查询自动过滤、removeById 自动改 UPDATE
- 代码中**禁止**手写 `delete_flag` 条件、**禁止**物理 delete 语句（物理删除有专门的 CommonDeleteAbsoluteMapper，慎用）
- ⚠️ 唯一索引与逻辑删除冲突时：不要建数据库唯一索引，改为 Service 层查询判重

## 菜单与按钮资源 SQL（SYS_RESOURCE）

新功能的菜单 + 按钮权限写 INSERT（权威格式见 gen 插件 sqlend 模板）：

```sql
-- 菜单（MENU 行）
INSERT INTO `SYS_RESOURCE` VALUES ('菜单id雪花串', '父目录id', '供应商管理', 'supplier', '菜单编码', 'MENU', '所属模块id', 'MENU', '/biz/supplier', 'biz/supplier/index', '图标', NULL, 'YES', 'YES', 'YES', 99, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
-- 按钮（BUTTON 行，parent = 菜单id；code = 驼峰按钮码，前端 hasPerm 用）
INSERT INTO `SYS_RESOURCE` VALUES ('按钮id雪花串', '菜单id雪花串', '新增供应商', NULL, 'bizSupplierAdd', 'BUTTON', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 'NOT_DELETE', NULL, NULL, NULL, NULL);
INSERT INTO `SYS_RESOURCE` VALUES ('...', '菜单id雪花串', '编辑供应商', NULL, 'bizSupplierEdit', 'BUTTON', ...);
INSERT INTO `SYS_RESOURCE` VALUES ('...', '菜单id雪花串', '删除供应商', NULL, 'bizSupplierDelete', 'BUTTON', ...);
```

- 按钮码固定集合：`{classNameFirstLower}Add / Edit / Delete / Detail / BatchDelete / Import / Export`（+ 业务动作码如 `UpdateStatus`）
- 需要字典数据时：`INSERT INTO DEV_DICT`（系统字典）或 BIZ 字典管理界面维护
- 菜单 SQL 执行到 **snowy-cloud** 库（web-app、biz-app、网关共用这一个业务库）
- 执行后：重启 snowy-biz-app（新接口代码在它里面）→ 角色管理给角色"授权资源"（菜单+按钮）与"授权权限"（接口 URL）→ 重新登录 → 前端可见

### 菜单挂载点速查（出厂种子的真实 ID，写 SQL 直接用）

新业务功能默认挂 **业务模块**（MODULE_ID=`1548901111999773976`）：

| 挂载点 | ID | 说明 |
|---|---|---|
| 业务模块（MODULE） | `1548901111999773976` | 新业务菜单的 MODULE_ID 用这个 |
| 公司架构目录（CATALOG） | `1548901111999773977` | 组织/人员类业务挂这个目录下 |
| 业务模块顶层 | PARENT_ID 填 `'0'` | 通用做法：独立业务直接挂模块顶层（同"通知公告""业务字典"） |

系统模块（`1548901111999770525`）下的目录仅平台功能使用：组织架构 `1548901111999770726`、权限管控 `1548901111999771126`、基础工具 `1548901111999771626`、系统运维 `1548901111999772126`、在线开发 `1548901111999773250`。

> 依据：`snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql` 种子数据（以上 ID 已在该文件中逐个核实；全新导入即生效）。若库已被人工调整过，用 `SELECT ID, TITLE, CATEGORY, PARENT_ID FROM SYS_RESOURCE` 核实后再用。前端菜单 component 路径 `biz/{域名}/index` 必须与 views 目录一致。

## 数据库连接与多数据源

- 库清单（同一个 MySQL 实例上三个库，用途互不相同）：

| 库名 | 用途 | 建库/初始化 |
|---|---|---|
| `snowy-cloud` | **业务库**：web-app、biz-app、网关共用，33 张平台表 + 新增 BIZ_ 表都在这里 | 先手工建库（utf8mb4），再导入 `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql`（脚本**不含** CREATE DATABASE） |
| `snowy-nacos-app-open` | Nacos 自身存储（配置中心数据） | 手工建库后导入 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/nacos-db.sql`；库名以 `snowy-server/snowy-nacos-app/src/main/resources/application.properties` 的 `db.url.0` 为准（README 写 snowy-nacos-app，不一致） |
| `snowy-cloud-job` | xxl-job 调度中心 | `snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql`（脚本自带 CREATE DATABASE） |

- ⚠️ 注意：`META-INF/nacos-db.sql` 已含全部 `config_info`（id 1~17、45，内容与 `config-data` 一致），新装只导它；`META-INF/nacos-db-snowy-config-init.sql` 是同 id 的同一批配置记录，仅用于刷新已有库，执行前需先删除同 id 记录，否则主键冲突
- 连接配置**不在本地 application.yml**（本地只做 Nacos 接入）：web-app、biz-app 的数据源在 Nacos 配置 `snowy-web-app-dev.yaml` / `snowy-biz-app-dev.yaml` 的 `spring.datasource.dynamic.datasource.master` 段；仓库源文件在 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/{dev,test,prod}/`，运行时以 Nacos 控制台（`http://localhost:8848/nacos`，命名空间 snowy-cloud-dev、分组 dev）里的同名配置为准（**AI 需要连接串时从这里动态解析，禁止硬编码**）
- 网关也连业务库：`snowy-gateway-app-dev.yaml` 的 `spring.datasource`（非 dynamic）
- 多数据源：dynamic-datasource，默认 master；oracle/mssql/dm/kingbase 配置已在上述 Nacos 配置里注释预留，取消注释即可加库
- Druid 监控页：web-app 的 `/druid`（网关有 `/druid/**` → web-app 的路由；出厂默认关闭，需要时把 `snowy-web-app-dev.yaml` 的 `stat-view-servlet.enabled` 改为 true，账号同在该段）
- 初始化脚本：`snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql`（33 张表）；新增表的 DDL 也建议在同目录 `_sql/` 下追加增量脚本文件

## AI 执行 SQL 的方式（降级链）

1. mysql CLI 可用 → 直接执行到 snowy-cloud 库（从上述 Nacos 配置解析连接）
2. CLI 不可用 → SQL 写入 `docs/sql-pending/{日期}-{功能}.sql`，明确提示用户手动执行
3. ❌禁止在没有明确用户确认时执行 DROP/TRUNCATE/ALTER 之类破坏性语句

## AI 自检 SQL 集（开发流程中随用随查）

```sql
-- 表建好了吗
SHOW TABLES LIKE 'BIZ_XXX';
-- 表结构（写 Entity 前核对字段）
SHOW CREATE TABLE BIZ_XXX;

-- 菜单/按钮插进去了吗（配合挂载点速查的 ID）
SELECT ID, TITLE, CATEGORY, PARENT_ID, CODE FROM SYS_RESOURCE WHERE CODE LIKE 'bizXxx%' OR ID = '菜单id';
-- 按钮码与前端 hasPerm 是否一一对应
SELECT CODE, TITLE FROM SYS_RESOURCE WHERE CATEGORY = 'BUTTON' AND PARENT_ID = '菜单id';

-- 验证落库与逻辑删除（配合 api-verify 接口自测）
SELECT ID, NAME, DELETE_FLAG, CREATE_TIME FROM BIZ_XXX ORDER BY CREATE_TIME DESC LIMIT 3;

-- 字典建好了吗
SELECT * FROM DEV_DICT WHERE CODE = 'XXX_DICT_CODE';
-- 默认密码配置（出厂值 Snowy@2026!）
SELECT CONFIG_VALUE FROM DEV_CONFIG WHERE CONFIG_KEY = 'SNOWY_SYS_DEFAULT_PASSWORD_FOR_B';
```

## 现有 33 表速查（按前缀分组）

| 前缀 | 表 |
|---|---|
| SYS_（12） | SYS_USER、SYS_USER_EXT、SYS_USER_PASSWORD、SYS_ORG、SYS_ORG_EXT、SYS_POSITION、SYS_ROLE、SYS_RESOURCE（模块/菜单/按钮都在这张表）、SYS_RELATION（用户-角色、角色-资源、角色-权限等关系统一存这里）、SYS_GROUP、SYS_USER_DATA_SCOPE、SYS_USER_DATA_SCOPE_MAP |
| BIZ_（1） | BIZ_NOTICE |
| DEV_（12） | DEV_CONFIG、DEV_DICT、DEV_EMAIL、DEV_FILE、DEV_JOB、DEV_LOG、DEV_MESSAGE、DEV_PUSH、DEV_RELATION、DEV_SLIDESHOW、DEV_SMS、DEV_WEAK_PASSWORD |
| GEN_（2） | GEN_BASIC、GEN_CONFIG |
| CLIENT_（4） | CLIENT_USER、CLIENT_USER_EXT、CLIENT_USER_PASSWORD、CLIENT_RELATION |
| MOBILE_（1） | MOBILE_RESOURCE |
| AUTH_（1） | AUTH_THIRD_USER |

（准确清单以 `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql` 为准，可 grep `CREATE TABLE`；Nacos、xxl-job 的表在各自库里，不在此列）

## 检查清单

- [ ] 表名 BIZ_ 前缀全大写，有表 COMMENT
- [ ] ID varchar(20) 主键，非自增
- [ ] 尾部审计字段组齐全且顺序标准
- [ ] 每字段中文 COMMENT
- [ ] 树形表有 PARENT_ID；一对多有外键字段（varchar(20)）
- [ ] 菜单/按钮 SQL 与代码 URL、前端 hasPerm 码一致
- [ ] DDL、菜单 SQL 都执行在 snowy-cloud 库（不是 Nacos 库或 xxl-job 库）
- [ ] DDL 增量脚本落 `snowy-modules/snowy-web-app/src/main/resources/_sql/`

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-modules/snowy-web-app/src/main/resources/_sql/snowy_mysql.sql` | 全部建表语句（BIZ_NOTICE 是标准范本） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/entity/BizNotice.java` | Entity 与表的映射范本 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonEntity.java` | 审计字段基类 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/enums/CommonDeleteFlagEnum.java` | 逻辑删除枚举 |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/sqlend/Mysql.sql.btl` | 菜单/按钮 SQL 权威模板 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml`、`snowy-biz-app-dev.yaml` | 业务库数据源配置（Nacos 配置源文件） |
| `snowy-server/snowy-nacos-app/src/main/resources/application.properties` | Nacos 自身存储库连接（db.url.0） |
| `snowy-server/snowy-xxl-job-app/src/main/resources/db/tables_xxl_job.sql` | xxl-job 库初始化脚本 |
