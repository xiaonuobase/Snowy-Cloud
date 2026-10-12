---
name: dict-config
description: Snowy 字典/配置/枚举三件套规范：DEV_DICT 系统字典与 BIZ 业务字典双轨、前端 DictSelect 组件、@Trans 字典翻译、业务枚举类规范、DEV_CONFIG 系统配置与 Nacos 配置的区别。触发场景：1) 字段需要下拉选项（状态/类型）2) 需要系统参数配置 3) 定义业务枚举 4) biz-app 里用字典翻译/读系统配置（跨服务）。触发词：字典、dict、DEV_DICT、BIZ_DICT、DictSelect、下拉、枚举、enum、系统配置、DEV_CONFIG、参数配置、Nacos 配置、easy-trans、跨服务字典、DevConfigApi。
---

# Snowy 字典 / 配置 / 枚举规范

## 三种"可选项"怎么选

| 场景 | 用什么 | 存哪 |
|---|---|---|
| 选项需要最终用户在界面维护 | **字典**（DEV_DICT / BIZ 字典） | 数据库 |
| 选项与代码逻辑强绑定（不同值走不同分支） | **枚举类**（modular/{域}/enums/） | Java 代码 |
| 运行时可调的参数（开关/阈值） | **系统配置** DEV_CONFIG | 数据库（界面改） |
| 服务启动级基础设施参数（数据源/Redis/端口/Sa-Token/`snowy.config.common.*`） | **Nacos 配置** | Nacos `snowy-{应用名}-dev.yaml`（仓库源文件在 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/`） |

## 字典双轨

| 轨道 | 模块 | 用途 | 管理界面 |
|---|---|---|---|
| 系统字典 | `snowy-plugin-dev-func` 的 `dev/modular/dict/`（DEV_DICT 表，**运行在 web-app**） | 平台级通用字典（性别 GENDER、通知类型等）；`/dev/dict/tree` 免登录（前端登录页也要用；网关 `/api/webapp/dev/dict/tree` 与服务端白名单两处都已放行） | 开发工具 → 字典管理 |
| 业务字典 | `snowy-plugin-biz-func` 的 `biz/modular/dict/`（同一张 DEV_DICT 表，`CATEGORY=BIZ` 区分，**运行在 biz-app**，接口 `/biz/dict/*`） | 业务自定义字典（面向最终用户可维护） | 业务功能 → 业务字典 |

**新业务字典优先建业务字典**（不污染平台字典）；平台级通用（性别这类）才进 DEV_DICT。

## 字典使用全链路

```
1. 界面建字典（编码如 SUPPLIER_TYPE，子项 值+标签+排序）
2. Entity 字段存字典"值"（String）：
     @Schema(description = "供应商类型")
     private String type;
3. 列表返回需要显示文本时，Entity 加 @Trans 翻译（向后兼容多一个冗名字段）：
     @Trans(type = TransType.DICTIONARY, key = "SUPPLIER_TYPE")
     private String type;
     @TableField(exist = false)
     private String typeName;        // 注意：@Trans DICTIONARY 自动生成翻译；SIMPLE 才用 ref
4. 前端下拉用 DictSelect 组件（自动拉字典并缓存；字典树来自 web-app 的 /dev/dict/tree，前端经 apiAdaptive 自动走 /api/webapp）：
     <dict-select v-model:value="formData.type" dict-type-code="SUPPLIER_TYPE" />
5. 查询条件：PageParam 的 type 字段 eq 精确匹配
```

## 在 biz-app 里用字典 / 翻译（微服务现状）

| 能力 | 现状（以代码为准） |
|---|---|
| `@Trans(type = TransType.DICTIONARY)` | easy-trans 字典缓存走 Redis：web-app 启动时 `DevDictServiceImpl` 调 `makeUseRedis()` + `refreshCache` 写入；biz-app 侧只有编辑业务字典时 `BizDictServiceImpl` 才调用 `makeUseRedis()`。biz-app 的 easy-trans 配置为 `is-enable-redis: true`、`is-enable-cloud: true`（web-app 为 `is-enable-cloud: false`）。⚠️ biz-app 内字典翻译能否稳定生效**未验证，使用前请先确认**（如看 BizUser 列表的 genderName 是否有值） |
| `CommonEntity` 的创建人/更新人名称 | `@Trans(type = TransType.RPC, serviceName = "snowy-web-app")`，biz-app 经 easy-trans 远程调 web-app 的 `/easyTrans/proxy/**`（两服务都已放入免登录名单） |
| 注入 `DevDictApi` | ⚠️ dev-api-context 里**没有** DevDictApiContextBean，在 biz-app 注入会缺 Bean（推导）；dev-feign 的 DevDictFeign 只有 getDictLabel 一个方法 |
| 业务字典（BizDictService） | 就在 biz-func 本地，biz-app 内直接用 |

## 业务枚举规范（modular/{域}/enums/）

```java
/*
 * ... 版权头
 */
package vip.xiaonuo.biz.modular.supplier.enums;

import lombok.Getter;
import vip.xiaonuo.common.exception.CommonException;

/**
 * 供应商状态枚举
 *
 * @author 你的名字
 * @date  2026/08/18 10:00
 */
@Getter
public enum BizSupplierStatusEnum {

    /** 启用 */
    ENABLE("ENABLE"),

    /** 禁用 */
    DISABLE("DISABLE");

    private final String value;

    BizSupplierStatusEnum(String value) {
        this.value = value;
    }

    /** 校验值合法性（入参来自前端时校验用） */
    public static void validate(String value) {
        boolean flag = ENABLE.getValue().equals(value) || DISABLE.getValue().equals(value);
        if(!flag) {
            throw new CommonException("不支持的状态：{}", value);
        }
    }
}
```

- 纯枚举 + `@Getter` + `private final String value` + 构造器（**无公共基类**）；需要校验时加静态 validate
- 命名 `Xxx{含义}Enum`，值用 String 大写（与字典值风格一致）
- 代码里取值 `BizSupplierStatusEnum.ENABLE.getValue()`，**禁止魔法字符串**散落
- 参考现有：`BizNoticeStatusEnum`（`biz/modular/notice/enums/`）

## 系统配置 DEV_CONFIG

- 界面：开发工具 → 系统配置（键值对，分组）
- 读取：

```java
@Resource
private DevConfigApi devConfigApi;

String value = devConfigApi.getValueByKey("BIZ_XXX_SWITCH");
```

- 跨服务：DEV_CONFIG 由 web-app 的 dev-func 管理；biz-app 注入 `DevConfigApi` 时实际是 dev-api-context 的 `DevConfigApiContextBean` 经 Feign 调 web-app（已装配，可直接用）；读取结果按配置键缓存在共享 Redis
- ❌ 不要把可调参数写死在代码或 Nacos 配置（改起来要重启）

### DEV_CONFIG 与 Nacos 配置的区别

| | DEV_CONFIG（系统配置） | Nacos 配置 |
|---|---|---|
| 存哪 | 业务库 `snowy-cloud` 的 DEV_CONFIG 表 | Nacos（命名空间 `snowy-cloud-dev`，data-id `snowy-{应用名}-dev.yaml`） |
| 谁读 | 所有服务经 `DevConfigApi.getValueByKey` | 各服务启动时作为 Spring 配置加载 |
| 放什么 | 业务开关/阈值、文件/短信/邮件/推送引擎参数等运行时可调项 | 数据源、Redis、端口、Sa-Token、springdoc 分组、`snowy.config.common.*` 等基础设施 |
| 改了怎么生效 | 界面修改后清除该键缓存，下次读取即为新值 | 项目未使用 `@RefreshScope`，建议重启对应服务（是否热生效未验证） |

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| 状态字段用 Integer 0/1 魔法值 | String + 枚举/字典（与平台风格一致） |
| 前端手写 options 数组存字典项 | DictSelect 组件（统一缓存） |
| 可调开关写死代码 | DEV_CONFIG + DevConfigApi |
| 平台字典里建业务选项 | 业务字典（biz） |
| 字典改了页面不刷新 | 前端字典有缓存，刷新页面/重新拉取；后端 easy-trans 翻译缓存由 DevDictServiceImpl / BizDictServiceImpl 增改时直接刷新（Redis） |
| biz-app 里直接注入 DevDictApi | 先确认 Bean 是否存在（出厂无 ContextBean） |
| 业务开关写进 Nacos 配置 | Nacos 只放基础设施配置，业务开关走 DEV_CONFIG |

## 检查清单

- [ ] 可维护选项进字典（业务字典优先），逻辑绑定选项进枚举
- [ ] Entity 翻译字段用 @Trans，冗余字段 @TableField(exist = false)
- [ ] 前端 DictSelect 的 dict-type-code 与字典编码一致
- [ ] 枚举按上方规范（纯枚举、无公共基类），无魔法字符串
- [ ] 运行时参数走 DEV_CONFIG，基础设施参数走 Nacos
- [ ] biz-app 里用到的字典翻译 / Dev*Api 已确认可用

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/dict/` | 系统字典模块（web-app） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/dict/` | 业务字典模块（biz-app） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/enums/BizNoticeStatusEnum.java` | 枚举范本 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevConfigApi.java` | 配置读取接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api-context/src/main/java/vip/xiaonuo/dev/api/context/` | biz-app 可用的 Dev*Api 远程代理清单 |
| `snowy-admin-web/src/components/DictSelect/` | 字典下拉组件（含 README） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/user/entity/BizUser.java` | @Trans DICTIONARY 实战 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-biz-app-dev.yaml` | biz-app 的 easy-trans 配置（web-app 同目录） |
