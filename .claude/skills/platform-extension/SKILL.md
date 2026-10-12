---
name: platform-extension
description: Snowy 微服务版平台扩展点地图：不改框架代码实现定制的所有正规途径——数据变更监听、跨插件/跨服务 api 复用、provider 与 Feign 五件套暴露、定时任务、系统配置、CommonWrapper、网关与服务两层白名单、前端复用，并标注各扩展点在 web-app / biz-app 的可用范围。触发场景：1) 用户说"我想改/增强平台某行为"（如用户变更后同步业务缓存、给某接口加默认逻辑）2) 判断一个定制需求该新建业务域还是扩展平台 3) 想复用平台能力（用户/字典/文件/消息）不知道从哪接。触发词：扩展、增强、定制、改平台、改框架、钩子、监听、联动、同步缓存、复用平台、不动源码、覆盖、事件、跨服务联动、Feign 复用、biz-app 能不能用。注意：新建业务功能走 crud-development/dev；本技能讲"平台行为定制"的正规入口；模块与服务间通信细节见 plugin-architecture。
---

# Snowy 微服务版平台扩展点地图

## 核心原则

> **平台插件（sys/auth/dev/gen/client/mobile，以及 mocker）原则上不改**——改了升级即冲突。几乎所有"改平台行为"的需求都有正规扩展点。先查本表，再决定动不动框架。
>
> 微服务版额外要先问一句：**这段代码跑在哪个服务？** 平台插件实现都装在 snowy-web-app（9101），业务代码（snowy-plugin-biz-func）跑在 snowy-biz-app（9102），不少扩展点只在本服务 JVM 内生效。

## "我想做 X" → 扩展点速查表

| 需求 | 正确姿势 | 位置 / 适用范围 |
|---|---|---|
| 用户/组织/角色变更后刷新**我方缓存或联动业务** | 实现 `CommonDataChangeListener` + @Component，按 dataType 分发 | 我的插件 `core/listener/`；⚠️ 事件只在发布方所在服务内分发，见扩展点 1 |
| 在业务代码里用平台能力（查用户/发消息/传文件/读配置） | 注入现成 `*-api` 接口（SysUserApi/DevMessageApi/DevFileApi/DevConfigApi...） | biz-app 里只能注入已装配远程代理（ContextBean）的接口，见扩展点 2 与 plugin-architecture 的现成 API 速查表 |
| 把我方能力暴露给其他插件 | 定义接口进我方 `{p}-api` + 我方 `provider/` 实现；需要被**其他服务**调用时再补 -feign / -feign-provider / -api-context 并在两端应用装配 | provider/ + 五件套（见 plugin-architecture） |
| 周期性任务（对账/清理/汇总） | 实现 `CommonTimerTaskRunner` + @Component + 界面配置 | ⚠️ DEV_JOB 只在 web-app 调度，Runner 必须在 web-app 类路径上；写在 biz-func 里的 Runner 会报"找不到对应的类"；跑在 biz-app 的定时需求无现成方案（xxl-job 只有调度中心、未接执行器），需另行评估接入方式，使用前请先确认 |
| 可调的业务参数（开关/阈值/默认密码） | DEV_CONFIG（系统配置界面维护），代码里 DevConfigApi 读取 | 界面：开发工具→系统配置；biz-app 有 DevConfigApi 的远程代理 |
| 下拉选项数据 | 业务字典（BIZ）/枚举（见 dict-config） | snowy-plugin-biz-func 的 biz/modular/dict 或 enums/；⚠️ biz-app 无 DevDictApi 远程代理 |
| 返回给前端的对象统一加工/包装 | `@CommonWrapper(包装类.class)`（返回值包装 AOP） | 注解在 snowy-base/snowy-common/annotation，AOP 在两服务 GlobalConfigure 各有一份 |
| 接口防重复提交 | `@CommonNoRepeat` | Controller 方法上（两服务均生效） |
| 操作留痕 | `@CommonLog("中文")` | Controller 写方法上；⚠️ 落 DEV_LOG 的 DevLogAop 只在 web-app，biz-app 里不落库（推导） |
| 字段展示为字典文本/关联名 | Entity 加 `@Trans`（DICTIONARY/SIMPLE） | entity/ |
| 新页面/新菜单 | views/biz/{域} + SYS_RESOURCE 资源 SQL | 前端 + database-ops 挂载点速查；`/biz/` 前缀已映射到 biz-app |
| 接口免登录 / C 端 / 超管路径 | 白名单两处都改：网关 `GatewayConfigure`（带 /api/webapp、/api/bizapp 前缀）+ 目标服务 `GlobalConfigure`（不带前缀） | 见 security-auth；只改一处会被另一层拦截 |
| 新的一级 URL 前缀 / 新服务 | 新前缀：前端 apiAdaptive.js 登记；新服务：另加网关路由；新代码包：另加 springdoc 分组与 Knife4j 聚合（只影响文档） | 见 plugin-architecture 挂载 checklist |
| 记录业务操作日志/异常排查 | web-app 内的 @CommonLog 已落 DEV_LOG，直接查；biz-app 的看服务日志 | 开发工具→日志；`logs/snowy-biz-app-log/` |

## 扩展点 1：数据变更监听（最高频）

平台（sys 等）改了共享数据（用户/组织/角色）会广播事件；我的插件想要联动（清缓存/同步数据），实现监听器即可，**零侵入**：

```java
/*
 * ... 版权头
 */
package vip.xiaonuo.biz.core.listener;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Component;
import vip.xiaonuo.common.listener.CommonDataChangeListener;

import java.util.List;

/**
 * XXX数据变化侦听器：监听平台对共享数据的变更，联动本插件逻辑
 *
 * @author 你的名字
 * @date  2026/08/18
 **/
@Component
public class BizXxxDataChangeListener implements CommonDataChangeListener {

    @Override
    public void doUpdateWithDataId(String dataType, String dataId) {
        // dataType 判别来源（自定义枚举），命中才处理
    }

    @Override
    public void doDeleteWithDataIdList(String dataType, List<String> dataIdList) {
    }

    // 接口共 10 个回调，按需覆写：Add/Update/Delete × WithDataId/WithDataIdList/WithData/WithDataList
    // （其中 Add/Update 有 WithData/WithDataList，Delete 只有 WithDataId/WithDataIdList）
}
```

- @Component 即自动注册（两服务的 GlobalConfigure 各自把本服务 Spring 容器中所有实现注册进 CommonDataChangeEventCenter）
- 平台侧发事件的写法（我的插件改了共享数据也要广播）：`CommonDataChangeEventCenter.doUpdateWithData(...)` 等——写在 Service 的增删改里
- dataType：跟随事件来源（BizDataTypeEnum 这类插件级枚举定义自己的类型）
- ⚠️ **事件不跨服务**：`CommonDataChangeEventCenter` 是进程内的静态监听器列表，没有经 Redis/MQ 转发。sys 的增删改发生在 web-app，事件只会分发给 web-app 里的监听器；biz-app 里的监听器（如出厂 `BizDataChangeListener`）只能收到 biz-app 自己发出的事件（如 BizOrg/BizGroup 的变更）。需要"web-app 数据变了 → biz-app 联动"时目前无现成机制，需另行设计方案，使用前请先确认

## 扩展点 2：复用平台能力（注入 *-api）

```java
@Resource
private SysUserApi sysUserApi;          // 用户
@Resource
private DevConfigApi devConfigApi;      // 系统配置（业务参数唯一正解）
@Resource
private DevFileApi devFileApi;          // 文件
@Resource
private DevMessageApi devMessageApi;    // 站内信（自动实时推送）
```

- 业务代码写法与单体版相同，只注入接口；在 biz-app 里实际注入的是 `XxxApiContextBean`，经 Feign 调 web-app，请求头（含 token）与 traceId 由 FeignRequestInterceptor 透传
- ⚠️ biz-app 当前**没有**远程代理的接口：DevDictApi、DevLogApi、DevJobApi、DevPushApi、DevWeakPasswordApi、SysMenuApi、SysButtonApi、SysModuleApi、ClientUserApi。按现有装配方式在 biz-app 注入它们会缺 Bean，使用前请先确认（补齐方式见 plugin-architecture 五件套）
- ⚠️ 跨服务调用无 fallback、无分布式事务：远程失败直接抛异常，`@Transactional` 回滚不了 web-app 侧已提交的写操作

完整清单见 plugin-architecture 技能"现成 API 速查"表。**先查有没有现成 api（以及 biz-app 有没有对应 ContextBean），再考虑自己写。**

## 什么情况下才真的要改框架

满足全部三条才动：
1. 扩展点表里确实没有对应姿势
2. 平台行为本身要变（不是"加上我的业务"，而是"改掉平台默认"）
3. 用户明确知晓升级冲突风险并接受

改法约束：优先"同包同名 @Configuration 覆盖 Bean/条件装配"，其次最小 diff 修改；改动必须记入 docs 并在 /sync 报告中标注"框架改动点"。微服务版还要写清改动落在哪个服务（web-app / biz-app / 网关 / Nacos 配置），改完重启对应服务。

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| 直接在 SysUserService 里加业务逻辑 | biz 插件建自己的域，跨插件走 SysUserApi |
| 业务开关写死在代码/本地 yml | DEV_CONFIG + DevConfigApi 读取（服务级配置放 Nacos） |
| 改共享数据后不管缓存 | 发 CommonDataChangeEventCenter 事件（注意只在本服务内分发） |
| 想收平台变更通知却去改 sys 源码 | 实现 CommonDataChangeListener（并确认监听器与发布方在同一服务） |
| 在 biz-func 里写 CommonTimerTaskRunner 指望界面配置就能跑 | 定时 Runner 只在 web-app 类路径上生效，biz-app 定时需求先确认方案 |
| 在 biz-app 注入 DevDictApi 等没有 ContextBean 的接口 | 先查 api-context 模块里有没有对应 ContextBean |
| 只改服务 GlobalConfigure 放开免登录 | 网关 GatewayConfigure + 服务 GlobalConfigure 两处 |
| 每个新需求都想新建插件或新服务 | 默认进 snowy-plugin-biz-func 的 modular（见 plugin-architecture） |

## 检查清单

- [ ] 需求先过了扩展点速查表，并确认代码跑在哪个服务
- [ ] 监听器/provider 放对位置（core/listener、provider/），监听器与事件发布方在同一服务
- [ ] 改共享数据后有广播事件
- [ ] biz-app 里注入的平台 Api 都有对应 ContextBean
- [ ] 未修改平台插件（sys/auth/dev/gen/client/mobile/mocker）的任何文件（或已按"三条全满足"记录改动）

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/core/listener/BizDataChangeListener.java` | 监听器官方范本（监听 biz 机构变更清 biz 机构缓存，运行在 biz-app） |
| `snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/listener/SysDataChangeListener.java` | biz-app 内的机构变更联动登录用户数据范围缓存 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/listener/CommonDataChangeEventCenter.java` | 事件中心（注册/广播，进程内） |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/listener/CommonDataChangeListener.java` | 监听接口（全部回调） |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/annotation/CommonWrapper.java` | 返回包装注解 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/job/task/DevJobTimerTaskRunner.java` | 定时任务扩展点范本（web-app） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/` | 可注入的平台能力全集（web-app 全可用） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api-context/src/main/java/vip/xiaonuo/dev/api/context/` | biz-app 可注入的 dev 能力（远程代理清单） |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-api-context/src/main/java/vip/xiaonuo/sys/api/context/` | biz-app 可注入的 sys 能力（远程代理清单） |
| `snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/GatewayConfigure.java` | 网关白名单 |
