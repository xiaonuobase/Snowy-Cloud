---
name: code-generator
description: Snowy 自带 Beetl 代码生成器的使用规范：入口流程、GenBasic 字段填写、5 种模板形态选择、sqlend 菜单 SQL、生成后修补清单。触发场景：1) 用户想用代码生成器生成功能 2) /dev 命令模式 B 引导用户走生成器 3) 选择生成模板形态（表格/树/左树右表/主子表）4) 生成菜单资源 SQL。触发词：代码生成、生成器、gen、Beetl、模板、genType、sqlend、导入表、一键生成、生成到 biz-func、生成后网关404。注意：AI 直接手写六件套的规范见 crud-development；本技能讲平台自带生成器的正确用法。
---

# Snowy 代码生成器使用规范

## 概览

Snowy 自带 Beetl 模板代码生成器（`snowy-plugin-gen`，实现在 `snowy-plugin-gen-func`，**只装在 snowy-web-app 里运行**），能从数据库表生成**后端六件套 + 前端三件 + 菜单资源 SQL** 的全套代码。这是开发标准 CRUD 的首选方式（对应 `/dev` 命令的**模式 B**）。

**入口**：启动 nacos、snowy-web-app、网关和前端 → 登录前端（9000）→ 开发工具 → 代码生成。数据存在 `GEN_BASIC` / `GEN_CONFIG` 表（库 snowy-cloud）。

**生成产物 = 本项目 CRUD 标准结构**（生成器模板就是官方规范的定义源，手写代码要与生成产物一致）。

**生成器不做的事（微服务版需手工补）**：不生成 provider / feign / feign-provider / api-context，不改网关路由、Knife4j 聚合配置、Nacos 配置，也不改前端 apiAdaptive.js。

## 使用流程（5 步）

```
1. 建表（按 database-ops 规范：BIZ_ 前缀、大写、String 雪花主键、审计字段齐全）
2. 代码生成页面 → 导入表（选择刚建的表，可按住 Ctrl 选主表+子表做主子表）
3. 填写 GenBasic 基础配置（下表）
4. 在字段配置页调整每个字段的：是否查询/显示/必填、控件类型、字典编码等
5. 点"生成代码"→ 压缩包下载（ZIP）或直接写入项目（PRO，项目内），取决于 generateType → 按"生成后必做"落地
```

**两种生成方式的输出位置**（`GenBasicServiceImpl.execGenPro`）：

| generateType | 后端输出 | 前端输出 | 菜单 |
|---|---|---|---|
| `ZIP` 压缩包（表单默认） | 压缩包内，需手工放到 `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/` 下 | 压缩包内，手工放到 `snowy-admin-web/src/` | 需手工执行 sqlend 产出的 SQL |
| `PRO` 项目内 | `{user.dir}/snowy-plugin/{pluginName}/{pluginName}-func/src/main/java/` + 包路径 `vip/xiaonuo/{moduleName}/modular/{busName}/` | `{user.dir}/snowy-admin-web/src/` | 自动插入菜单与按钮，并给超管角色授权这些菜单按钮及 `/{moduleName}/{busName}/*` 接口权限 |

⚠️ PRO 方式在 web-app 进程里写文件，`user.dir`（web-app 的工作目录）必须是仓库根目录，否则报"后端/前端代码生成位置不存在"。移动端代码只支持 ZIP。

## GenBasic 关键字段填写指南

| 字段 | 说明 | 业务插件二开的推荐值 |
|---|---|---|
| `dbTable` / `dbTableKey` | 主表名 / 主键（导入时自动带出） | — |
| `pluginName` | 生成代码归属插件（PRO 方式按 `snowy-plugin/{pluginName}/{pluginName}-func` 拼目录） | `snowy-plugin-biz`（表单默认值；填 `biz` 会找不到目录） |
| `moduleName` | 模块名（URL 第一段、包路径 `vip.xiaonuo.{moduleName}`、前端 api/views 目录都用它） | `biz`（必须能被前端 apiAdaptive.js 映射，`biz` 对应 `/api/bizapp`） |
| `tablePrefix` | 表前缀移除：`Y` 移除 / `N` 不移除（Y 时业务名去掉表名第一段） | `Y`（BIZ_SUPPLIER → busName `supplier`） |
| `generateType` | 生成方式：`ZIP` 压缩包 / `PRO` 项目内 | 二开推荐 ZIP 后自查合入 |
| `module` | 所属系统模块（SYS_RESOURCE 的 MODULE，决定菜单挂在哪） | 选业务所属模块 |
| `menuPid` | 上级菜单/目录 id | 选业务菜单挂载点 |
| `mobileModule` | 移动端所属模块（留空则不生成移动端） | 一般留空 |
| `functionName` | 功能名（中文，用于菜单标题/日志） | 如 `供应商` |
| `busName` | 业务名（小写，URL 与前端目录名） | 如 `supplier` → `/biz/supplier/page` |
| `className` | 类名（导入表时按**整个表名**转驼峰自动带出，不受 tablePrefix 影响） | BIZ_SUPPLIER → `BizSupplier` |
| `formLayout` | 表单布局 | 按需 |
| `gridWhether` | 是否栅格布局 | 按需 |
| `packageName` | 包名 | 默认 `vip.xiaonuo` |
| `authorName` | 作者（写进 Javadoc @author） | 你的名字 |
| `genType` | 模板形态（见下表） | 按业务选 |
| `treeParentField` / `treeNameField` | 树形态：父字段 / 显示名字段 | 仅 TREE 形态填 |
| `subDbTable` / `subDbTableKey` / `subForeignKey` / `subClassName` | 主子表：子表名/主键/关联外键/子类名 | 仅 MASTER_DETAIL 填 |

## 模板形态选择（genType）

| genType | 形态 | 适用 | 典型例子 |
|---|---|---|---|
| `TABLE` | 普通表格 | 标准 CRUD 列表 | 供应商管理、公告管理 |
| `TREE` | 树形表格 | 有 parent_id 的层级数据 | 分类树、地区树 |
| `LEFT_TREE_TABLE` | 左树右表 | 左边选分类右边列数据 | 按分类组织的商品 |
| `MASTER_DETAIL` | 主子表 | 一对多，主表+子表一起编辑 | 订单+订单明细 |

模板目录：`snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/` 下
`backend-{table,tree,left-tree-table,master-detail}` + `frontend-{table,tree,left-tree-table,master-detail}` + `mobile` + `sqlend`。

## sqlend：菜单资源 SQL（生成的关键产物之一）

生成器会产出 `SYS_RESOURCE` 的 INSERT 语句（模板：`snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/sqlend/Mysql.sql.btl`）：

- 1 条 MENU 行：`INSERT INTO SYS_RESOURCE VALUES ('${menuId}', '${parentId}', '${functionName}管理', '${busName}', '${menuCode}', 'MENU', '${moduleId}', 'MENU', '${menuPath}', '${menuComponent}', ...)` 
- N 条 BUTTON 行：按钮码为**驼峰式** `${classNameFirstLower}Add / Edit / Delete / Detail / BatchDelete / Import / Export`（如 `bizSupplierAdd`）——这些码就是前端 `hasPerm('bizSupplierAdd')` 用的按钮权限码
- 配置了 mobileModule 时另有 MOBILE_RESOURCE 行（按钮码 `mobile{ClassName}Add` 形态）

id 值由生成器渲染时生成（字符串雪花）。ZIP 方式下**菜单 SQL 必须执行入库**（到 snowy-cloud 库），否则前端看不到菜单、按钮权限全失效；PRO 方式已由生成器自动插入，不要重复执行。

## 生成后必做（AI 协助修补清单）

1. **落位**：确认文件在 `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/{busName}/` 与前端 `src/views/biz/{busName}/`、`src/api/biz/biz{ClassName}Api.js`（ZIP 方式要手工放到这些位置）
2. **版权头**：逐个 .java 检查 12 行 Apache 2.0 版权声明（生成器可能按 authorName 生成变体，缺失必补）
3. **执行菜单 SQL**（仅 ZIP 方式）：sqlend 产出的 INSERT 执行到 snowy-cloud 库
4. **重启 snowy-biz-app**（新代码在 biz-func，只有 biz-app 加载它）+ 刷新前端 → 系统管理 → 角色管理 → 给目标角色"授权资源"（菜单与按钮）和"授权权限"（`/biz/{busName}/*` 接口），否则普通用户 403/按钮不显示
5. **核对生成代码**：URL 是否 `/biz/{busName}/{动作}`、@SaCheckPermission 值是否等于 URL、Param 校验消息是否中文
6. **删除生成痕迹**：不需要的 import、注释里的模板变量残留
7. 涉及树/主子表时核对 treeParentField 与 subForeignKey 的实际业务正确性
8. **微服务补充步骤（生成器不做）**：
   - 前端：moduleName 为 `biz` 时 apiAdaptive.js 已映射，无需改；moduleName 不在映射表里（不是 sys/auth/client/dev/gen/mobile/biz）时必须在 `snowy-admin-web/src/utils/apiAdaptive.js` 登记，否则经网关 404；busName 不要取 sys/auth/client/dev/gen/mobile（映射按包含匹配，会被转到 `/api/webapp`）
   - 接口文档：包在 `vip.xiaonuo.biz` 下自动进 SNOWY-PLUGIN-BIZ 分组；用了别的 moduleName 时要在 biz-app 的 Nacos 配置加 springdoc 分组，并在网关 Nacos 配置 `knife4j.gateway.routes` 加聚合路由
   - 跨服务：生成的功能需要被其他服务调用时，手工补 provider + feign 五段并装配（步骤见 crud-development 技能）
   - ⚠️ 现状：生成的 Controller 写操作带 `@CommonLog`，但 biz-app 没有日志切面，目前不会落 DEV_LOG

## 生成器 API（程序化调用，进阶）

`GenBasicController`（`snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/java/vip/xiaonuo/gen/modular/basic/controller/GenBasicController.java`）提供：分页/详情/添加/编辑/删除/预览 preview/执行生成 execGenZip 等，路径前缀 `/gen/basic/*`（经网关为 `/api/webapp/gen/basic/*`，属仅超管路径）。AI 一般不直接调这些接口——引导用户走界面，AI 负责生成后的核对修补。

## 常见问题

| 问题 | 处理 |
|---|---|
| 导入表列表为空 | 检查 web-app 的数据源（Nacos 配置 `snowy-web-app-dev.yaml` 的 `spring.datasource.dynamic.datasource.master` 段，默认库 snowy-cloud）与表是否已建 |
| 生成后菜单不显示 | ZIP 方式菜单 SQL 没执行，或没给角色授权 |
| 按钮全不显示 | BUTTON 行的驼峰码与前端 hasPerm 不匹配，核对 SYS_RESOURCE 的 code 列 |
| 生成的类名不对 | className 是导入表时按整个表名转驼峰带出的，可在表单里直接改；tablePrefix 只影响 busName |
| PRO 方式报"后端/前端代码生成位置不存在" | pluginName 不是 `snowy-plugin-biz`，或 web-app 的工作目录不是仓库根目录 |
| 页面接口经网关 404 | moduleName/busName 前缀没被 apiAdaptive.js 正确映射，或 biz-app 未重启 |
| 想改生成模板 | 模板在 `snowy-plugin-gen-func/src/main/resources/` 下（.btl Beetl 文件），改后重启 web-app；属于改框架，谨慎 |

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/java/vip/xiaonuo/gen/modular/basic/entity/GenBasic.java` | 生成基础配置实体（全部字段） |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/java/vip/xiaonuo/gen/modular/basic/service/impl/GenBasicServiceImpl.java` | 生成逻辑（Beetl 渲染；execGenPro 拼输出目录、自动建菜单与授权） |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/sqlend/Mysql.sql.btl` | 菜单/按钮 SQL 模板（SYS_RESOURCE INSERT 的权威格式） |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/backend-table/` | 后端六件套模板 |
| `snowy-plugin/snowy-plugin-gen/snowy-plugin-gen-func/src/main/resources/frontend-table/` | 前端三件模板 |
| `snowy-admin-web/src/views/gen/basic.vue` | 生成器表单（pluginName/moduleName/tablePrefix/generateType 默认值） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/` | 与生成产物同构的手写范本 |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端接口前缀映射（生成后需核对） |
