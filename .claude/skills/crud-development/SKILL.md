---
name: crud-development
description: Snowy CRUD 全套开发规范（六件套 + 可选两件 + 前端三件）。触发场景：1) 开发新的业务模块/增删改查功能 2) 编写 Entity/Mapper/Service/Controller/Param/Result 任何一件 3) 仿照现有模块新建业务域。触发词：CRUD、增删改查、业务模块、新建模块、Entity、Service、Controller、Param、分页、六件套、biz-func、网关路径、feign、跨服务。注意：只讲"怎么写代码"；建表/菜单 SQL 见 database-ops；接口设计细节见 api-development；用自带生成器见 code-generator。
---

# Snowy CRUD 开发规范

## 架构特征表（先背下来再写代码）

| 特征 | Snowy 约定 | 与同类快速开发框架相反点 |
|---|---|---|
| 包名 | `vip.xiaonuo.biz.modular.{域名}.{层}` | 不是其他脚手架的 org.* / com.* 包名 |
| 依赖注入 | `@Resource`（jakarta.annotation）字段注入 | ❌@Autowired ❌构造器注入 |
| ServiceImpl | `extends ServiceImpl<XxxMapper, Xxx> implements XxxService` | ❌只 implements 不继承 |
| 对象转换 | Hutool `BeanUtil.toBean / copyProperties` | ❌MapstructUtils |
| Lombok | 只用 `@Getter @Setter` | ❌@Data |
| 主键 | `@TableId private String id`（字符串雪花） | ❌Long |
| URL | 动词式写方法上 `/biz/xxx/page`（不带网关前缀，经网关访问为 `/api/bizapp/biz/xxx/page`） | ❌RESTful 路径 ❌类级 @RequestMapping |
| 分页 | `CommonPageRequest.defaultPage()` + MP `Page<T>` | — |
| 版权头 | 每个 .java 头部 12 行 Apache 2.0 声明 | AI 生成最容易漏 |
| 代码位置 | `snowy-plugin-biz-func` 模块（运行在 snowy-biz-app，端口 9102） | 微服务版比单体多一层 `-func` 目录 |

## 版权头模板（每个新 .java 必须有）

```java
/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
```

## 目录结构（一个业务域 = modular 下一个目录）

```
snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/{域名}/
├── controller/XxxController.java
├── entity/Xxx.java
├── enums/XxxStatusEnum.java          （可选，有状态字段时）
├── mapper/XxxMapper.java
│   └── mapping/XxxMapper.xml         （可选，仅自定义 SQL）
├── param/XxxPageParam.java
│       XxxAddParam.java
│       XxxEditParam.java
│       XxxIdParam.java
├── result/XxxResult.java             （可选，仅投影返回）
├── service/XxxService.java
│   └── impl/XxxServiceImpl.java
└── provider/XxxApiProvider.java      （可选，被跨插件/跨服务调用时）
```

类名前缀 = 插件缩写（业务插件是 **Biz**）：`BizNotice`、`BizNoticeService`。

## 微服务要点：运行位置、访问路径、要不要 feign 化

**运行位置**：`snowy-plugin-biz-func` 只装配在 `snowy-biz-app`（9102）。在 biz-func 里新增一个业务域**不需要改任何 pom、网关路由、Knife4j 配置或前端前缀映射**——网关已有 `/api/bizapp/**` 路由，Knife4j 的 BIZ 分组扫描整个 `vip.xiaonuo.biz` 包，前端 apiAdaptive.js 已把 `/biz/` 映射到 `/api/bizapp`。

**同一个接口的三种路径**：

| 场景 | 路径 | 说明 |
|---|---|---|
| Controller 注解 / `@SaCheckPermission` 值 | `/biz/xxx/page` | 只写这个，不带任何网关前缀 |
| 经网关访问（前端、自测、Knife4j 调试） | `http://localhost:9003/api/bizapp/biz/xxx/page` | 网关 StripPrefix=2 去掉 `/api/bizapp` 后转发给 biz-app |
| 直连服务（仅排查用） | `http://localhost:9102/biz/xxx/page` | 绕过网关层校验，但服务内的登录/权限校验仍生效 |

**要不要 feign 化**：只有当**其他服务**（如 web-app 里的 sys/dev/auth）需要调用本域能力时才做；只给前端用、只在 biz-app 内部用的域**不做**。需要时按现有五段式补齐并装配（范本：机构主管转移 BizOrgDirectorTransfer 一组）：

1. `snowy-plugin-biz-api`：`vip.xiaonuo.biz.api.BizXxxApi` 接口，返回 `JSONObject` / `List<JSONObject>` / `Page<JSONObject>` / `String` / 基本类型，不返回 Entity
2. `snowy-plugin-biz-func`：`modular/{域名}/provider/BizXxxApiProvider`（@Service implements BizXxxApi）
3. `snowy-plugin-biz-feign`：`vip.xiaonuo.biz.feign.BizXxxFeign`，`@FeignClient(name = FeignConstant.BIZ_APP, contextId = "BizXxxFeign")`，路径 `/feign/biz/{域名}/{方法}`，复杂返回值用 String（JSON）
4. `snowy-plugin-biz-feign-provider`：`@RestController implements BizXxxFeign`，转调 BizXxxApi，结果 `JSONUtil.toJsonStr`
5. `snowy-plugin-biz-api-context`：`@Component implements BizXxxApi`，内部调 Feign 再用 JSONUtil 解析（调用方使用的远程代理）
6. 装配：biz-app 的 pom 加 `snowy-plugin-biz-feign-provider`；调用方应用的 pom 加 `snowy-plugin-biz-feign` + `snowy-plugin-biz-api-context`；调用方业务代码仍只注入 `BizXxxApi`

> ⚠️ 现状：biz 插件的 feign / feign-provider / api-context 子模块出厂只有 BizOrgDirectorTransfer 一组，且**未装配进任何应用**（biz-app 只装了 biz-func，web-app 没装 biz-api-context），这条远程链路目前不生效。
> ⚠️ 注意：同一应用里某插件要么装本地实现（-func），要么装远程代理（-feign + -api-context），**不能同时装**（如 sys-func 的 `@Service("loginUserApi")` 与 auth-api-context 的 `@Component("loginUserApi")` 同名冲突）。
> ⚠️ 已知风险（推导未验证）：各服务 GlobalConfigure 把 `/feign/**` 列为免登录，feign-provider 上没有 @SaCheckPermission，网关也未拦截 `/api/*/feign/**`，登录用户可经网关直调 feign 接口；新增 feign 接口前先评估暴露面。

**本域调用别的服务**：在 biz-func 里只注入对方的 `XxxApi`（如 `SysUserApi`），由 biz-app 已装的 api-context 远程代理完成调用，不要直接注入 Feign、不要依赖对方的 -func。⚠️ 已知缺口（使用前需确认）：biz-app 里 dev 侧只有 File/Dev/Config/Message/Slideshow/Sms/Email 七个 ContextBean，`DevDictApi`/`DevLogApi`/`DevJobApi`/`DevPushApi`/`DevWeakPasswordApi` 没有；sys 侧缺 `SysMenuApi`/`SysButtonApi`/`SysModuleApi`——在 biz-app 注入这些接口会缺 Bean（推导）。

## 第 1 件：Entity

```java
@Getter
@Setter
@TableName("BIZ_XXX")                      // 表名全大写；有 SM4 加密字段时 @TableName(value = "BIZ_XXX", autoResultMap = true)
public class BizXxx extends CommonEntity {

    /** 主键 */
    @TableId
    @Schema(description = "主键")
    private String id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortCode;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
}
```

要点：
- 继承 `CommonEntity`（自动获得 deleteFlag/createUser/createTime/updateUser/updateTime 审计字段，逻辑删除自动过滤）
- 敏感字段（手机号等）加 `@TableField(typeHandler = CommonSm4CbcTypeHandler.class)`，见 crypto-sm 技能
- 字典翻译字段加 `@Trans(type = TransType.DICTIONARY, key = "XXX")`，关联表翻译加 `@Trans(type = TransType.SIMPLE, target = Yyy.class, fields = "name", alias = "yyy", ref = "yyyName")`
- 每个字段有 `/** 中文说明 */` + `@Schema(description = "中文")`

## 第 2 件：Mapper（通常空接口）

```java
public interface BizXxxMapper extends BaseMapper<BizXxx> {
}
```

自定义 SQL 才建 `mapper/mapping/BizXxxMapper.xml`（namespace = Mapper 全限定名，与 Java 同包）。

## 第 3 件：Service 接口

```java
public interface BizXxxService extends IService<BizXxx> {

    /**
     * 获取XXX分页
     *
     * @author 你的名字
     * @date  2026/08/18 10:00
     */
    Page<BizXxx> page(BizXxxPageParam bizXxxPageParam);

    void add(BizXxxAddParam bizXxxAddParam);          // 每个方法都要中文 Javadoc + @author + @date
    void edit(BizXxxEditParam bizXxxEditParam);
    void delete(List<BizXxxIdParam> bizXxxIdParamList);
    BizXxx detail(BizXxxIdParam bizXxxIdParam);
    BizXxx queryEntity(String id);                    // 内部帮助方法：查不到抛异常
}
```

## 第 4 件：ServiceImpl（核心模板）

```java
@Service
public class BizXxxServiceImpl extends ServiceImpl<BizXxxMapper, BizXxx> implements BizXxxService {

    @Override
    public Page<BizXxx> page(BizXxxPageParam bizXxxPageParam) {
        QueryWrapper<BizXxx> queryWrapper = new QueryWrapper<BizXxx>().checkSqlInjection();
        // 每个查询条件都要判空
        if(ObjectUtil.isNotEmpty(bizXxxPageParam.getName())) {
            queryWrapper.lambda().like(BizXxx::getName, bizXxxPageParam.getName());   // 模糊
        }
        if(ObjectUtil.isNotEmpty(bizXxxPageParam.getType())) {
            queryWrapper.lambda().eq(BizXxx::getType, bizXxxPageParam.getType());     // 精确
        }
        if(ObjectUtil.isAllNotEmpty(bizXxxPageParam.getStartCreateTime(), bizXxxPageParam.getEndCreateTime())) {
            queryWrapper.lambda().between(BizXxx::getCreateTime,
                    bizXxxPageParam.getStartCreateTime(), bizXxxPageParam.getEndCreateTime());
        }
        // 排序：前端传了 sortField/sortOrder 用之，否则默认按 sortCode 升序
        if(ObjectUtil.isAllNotEmpty(bizXxxPageParam.getSortField(), bizXxxPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(bizXxxPageParam.getSortOrder());
            queryWrapper.orderBy(true, bizXxxPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(bizXxxPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(BizXxx::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)      // 写操作必须带
    @Override
    public void add(BizXxxAddParam bizXxxAddParam) {
        BizXxx bizXxx = BeanUtil.toBean(bizXxxAddParam, BizXxx.class);   // 新增：Param → Entity
        this.save(bizXxx);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(BizXxxEditParam bizXxxEditParam) {
        BizXxx bizXxx = this.queryEntity(bizXxxEditParam.getId());       // 编辑：先查再拷贝
        BeanUtil.copyProperties(bizXxxEditParam, bizXxx);
        this.updateById(bizXxx);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<BizXxxIdParam> bizXxxIdParamList) {
        this.removeByIds(CollStreamUtil.toList(bizXxxIdParamList, BizXxxIdParam::getId));
    }

    @Override
    public BizXxx detail(BizXxxIdParam bizXxxIdParam) {
        return this.queryEntity(bizXxxIdParam.getId());
    }

    @Override
    public BizXxx queryEntity(String id) {
        BizXxx bizXxx = this.getById(id);
        if(ObjectUtil.isEmpty(bizXxx)) {
            throw new CommonException("XXX不存在，id值为：{}", id);        // 中文消息 + {} 占位
        }
        return bizXxx;
    }
}
```

## 第 5 件：Controller（五接口模板）

```java
@Tag(name = "XXX控制器")
@RestController
@Validated
public class BizXxxController {

    @Resource
    private BizXxxService bizXxxService;

    @Operation(summary = "获取XXX分页")
    @SaCheckPermission("/biz/xxx/page")                      // 权限码 = 接口 URL
    @GetMapping("/biz/xxx/page")
    public CommonResult<Page<BizXxx>> page(BizXxxPageParam bizXxxPageParam) {
        return CommonResult.data(bizXxxService.page(bizXxxPageParam));
    }

    @Operation(summary = "添加XXX")
    @CommonLog("添加XXX")                                    // 写操作必须加操作日志
    @SaCheckPermission("/biz/xxx/add")
    @PostMapping("/biz/xxx/add")
    public CommonResult<String> add(@RequestBody @Valid BizXxxAddParam bizXxxAddParam) {
        bizXxxService.add(bizXxxAddParam);
        return CommonResult.ok();
    }

    @Operation(summary = "编辑XXX")
    @CommonLog("编辑XXX")
    @SaCheckPermission("/biz/xxx/edit")
    @PostMapping("/biz/xxx/edit")
    public CommonResult<String> edit(@RequestBody @Valid BizXxxEditParam bizXxxEditParam) {
        bizXxxService.edit(bizXxxEditParam);
        return CommonResult.ok();
    }

    @Operation(summary = "删除XXX")
    @CommonLog("删除XXX")
    @SaCheckPermission("/biz/xxx/delete")
    @PostMapping("/biz/xxx/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<BizXxxIdParam> bizXxxIdParamList) {
        bizXxxService.delete(bizXxxIdParamList);
        return CommonResult.ok();
    }

    @Operation(summary = "获取XXX详情")
    @SaCheckPermission("/biz/xxx/detail")
    @GetMapping("/biz/xxx/detail")
    public CommonResult<BizXxx> detail(@Valid BizXxxIdParam bizXxxIdParam) {
        return CommonResult.data(bizXxxService.detail(bizXxxIdParam));
    }
}
```

> ⚠️ 现状：`@CommonLog` 的落库切面 DevLogAop 在 dev-func 里，而 dev-func 只装在 web-app；biz-app 类路径上没有这个切面，biz 插件接口上的 `@CommonLog` 目前**不会写 DEV_LOG**（推导，未实际运行验证）。写操作仍保留 `@CommonLog`，与出厂 BizNoticeController 保持一致。

## 第 6 件：Param 组（每操作一个类）

- **XxxPageParam**：current/size/sortField/sortOrder/searchKey 五个固定字段 + 业务查询字段（无校验注解）
- **XxxAddParam**：业务字段 + `@NotBlank(message = "xxx不能为空")` 等校验（jakarta.validation）
- **XxxEditParam**：同 AddParam + `@NotBlank private String id`
- **XxxIdParam**：只有 id

```java
@Getter
@Setter
public class BizXxxIdParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotBlank(message = "id不能为空")
    private String id;
}
```

## 可选两件

- **Result**：仅需要投影/跨表组装返回时建 `result/XxxResult.java`（@Getter @Setter + @Schema）；直接返回 Entity 是允许的
- **Mapper XML**：仅自定义 SQL；空 XML 不要建

## 前端三件（见 frontend-pc 技能详解）

1. `snowy-admin-web/src/api/biz/bizXxxApi.js` —— API 封装（`baseRequest('/biz/xxx/' + url, ...)`，不要手写 `/api/bizapp` 前缀）
2. `snowy-admin-web/src/views/biz/xxx/index.vue` —— 列表页（s-table）
3. `snowy-admin-web/src/views/biz/xxx/form.vue` —— 弹窗表单

接口前缀映射：`src/utils/apiAdaptive.js`（`src/config/index.js` 里 `CLOUD_SERVER: true` 时生效）在 `baseRequest` 里把 `/biz/` 开头的地址转成 `/api/bizapp/biz/...`，`/sys/` `/auth/` `/client/` `/dev/` `/gen/` `/mobile/` 转成 `/api/webapp/...`，再拼上 `VITE_API_BASEURL`（网关 `http://localhost:9003`）。新业务域沿用 `/biz/` 前缀即可，无需改映射。

> ⚠️ 注意：映射用 `indexOf` 包含匹配、按数组顺序取第一个命中，`/biz/` 排在最后。业务域名**不要**取 `sys`、`auth`、`client`、`dev`、`gen`、`mobile`——例如 `/biz/dev/page` 会先命中 `/dev/` 被转到 `/api/webapp`，经网关 404。
> 新的 URL 前缀（不是 `/biz/` 开头）：必须在 apiAdaptive.js 登记到对应服务的网关前缀，否则请求不带 `/api/xxxapp`、经网关 404；代码包不在 `vip.xiaonuo.biz` 下时，还要在该服务的 Nacos 配置加 springdoc 分组、在网关 Nacos 配置加 Knife4j 聚合路由；新建一个应用则另需网关路由（`/api/xxxapp/**`，StripPrefix=2）。

## 常见错误正误对照

| ❌ 错误（同类框架惯性） | ✅ 正确（Snowy） |
|---|---|
| `@Autowired private XxxService x;` | `@Resource private XxxService x;` |
| `public class XxxServiceImpl implements IXxxService` | `extends ServiceImpl<XxxMapper, Xxx> implements XxxService` |
| `MapstructUtils.convert(bo, Xxx.class)` | `BeanUtil.toBean(param, Xxx.class)` |
| `@Data public class Xxx` | `@Getter @Setter public class Xxx` |
| `@RequestMapping("/biz/xxx")` 类级 + `@PostMapping("/list")` | 无类级注解，方法上直接 `@PostMapping("/biz/xxx/add")` |
| `@SaCheckPermission("biz:xxx:list")` | `@SaCheckPermission("/biz/xxx/page")` |
| `private Long id` / `@TableId(type = IdType.ASSIGN_ID)` Long | `@TableId private String id` |
| `throw new ServiceException("xxx")` | `throw new CommonException("xxx不存在，id值为：{}", id)` |
| 单个 XxxBo + AddGroup/EditGroup | XxxPageParam / XxxAddParam / XxxEditParam / XxxIdParam 四类 |
| `R.ok(data)` / `AjaxResult` | `CommonResult.data(data)` / `CommonResult.ok()` |
| `queryWrapper.eq("name", ...)` 字符串列名 | `queryWrapper.lambda().eq(Xxx::getName, ...)` |

## 检查清单（写完自查）

- [ ] 每个 .java 有 12 行版权头
- [ ] 文件落在 `snowy-plugin-biz-func` 下；包名 `vip.xiaonuo.biz.modular.xxx.*`，类名 Biz 前缀
- [ ] Entity 继承 CommonEntity，@TableName 大写，@TableId String
- [ ] ServiceImpl extends ServiceImpl 且写方法有 @Transactional(rollbackFor = Exception.class)
- [ ] 查询 QueryWrapper 带 checkSqlInjection()，条件全部判空
- [ ] Controller：@Tag/@RestController/@Validated，URL 动词式，@SaCheckPermission 值 = URL（不带网关前缀），写操作 @CommonLog
- [ ] Param 四类齐全，校验消息中文
- [ ] 所有注释/Javadoc/异常消息中文，Javadoc 有 @author @date
- [ ] 前端三件已同步生成，api js 用 `/biz/` 前缀且域名不与其他插件前缀撞名
- [ ] 跨服务：只注入 XxxApi；仅在需被其他服务调用时才补 feign 五段并装配

## 参考实现（真实代码，照这个写）

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/notice/` | 最干净完整的六件套范本（entity/mapper/service/impl/controller/param 全有，另含 provider） |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/controller/SysUserController.java` | 复杂版 Controller（含导出/禁用启用） |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/modular/user/service/impl/SysUserServiceImpl.java` | 复杂版 ServiceImpl（含 SM4 字段/事务/重复校验） |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/page/CommonPageRequest.java` | 分页请求构造 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/enums/CommonSortOrderEnum.java` | 排序枚举 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/pojo/CommonEntity.java` | 实体基类（审计字段） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-feign/src/main/java/vip/xiaonuo/biz/feign/BizOrgDirectorTransferFeign.java` | biz 侧 Feign 接口范本（`FeignConstant.BIZ_APP`、`/feign/biz/...`） |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-feign-provider/src/main/java/vip/xiaonuo/biz/feign/provider/transfer/BizOrgDirectorTransferFeignProvider.java` | Feign 服务端实现范本 |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-api-context/src/main/java/vip/xiaonuo/biz/api/context/transfer/BizOrgDirectorTransferApiContextBean.java` | 调用方远程代理范本 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api-context/src/main/java/vip/xiaonuo/sys/api/context/SysUserApiContextBean.java` | biz-app 调 web-app 的真实代理（BizUserServiceImpl → SysUserApi → 本类 → SysUserFeign） |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/FeignConstant.java` | Feign 服务名常量（WEB_APP / BIZ_APP） |
| `snowy-admin-web/src/utils/apiAdaptive.js` | 前端接口前缀 → 网关前缀映射 |
