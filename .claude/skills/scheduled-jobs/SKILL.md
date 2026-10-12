---
name: scheduled-jobs
description: Snowy 定时任务规范：CommonTimerTaskRunner 接口实现 + DEV_JOB 表配置 + Cron 表达式 + 任务执行日志；微服务版只在 snowy-web-app 调度。触发场景：1) 业务需要定时执行（对账/清理/汇总）2) 添加或排查定时任务 3) 写 Cron 表达式 4) 定时任务报"找不到对应的类"、biz-app 里要跑定时。触发词：定时任务、定时、Cron、调度、DEV_JOB、TimerTaskRunner、轮询、周期执行、任务日志、xxl-job、找不到对应的类、跨服务定时。
---

# Snowy 定时任务规范

## 机制（Snowy 自研 timer 体系，不是 SnailJob/xxl-job/quartz）

```
后端：写一个 CommonTimerTaskRunner 实现类（@Component），且必须在 snowy-web-app 的类路径上
界面：开发工具 → 定时任务 → 新增任务（DEV_JOB 表）
      填：任务所属处理器（下拉只列 web-app 里的 Runner Bean）、Cron、是否启用
执行：web-app 内 dev-func 的调度器（Hutool CronUtil）按 Cron 用 Class.forName(类名) + SpringUtil.getBean 调用 Runner.action(extJson)
日志：DEV_JOB 无执行记录表，执行情况看 web-app 应用日志（logs/snowy-web-app-log/）
```

## 微服务版运行位置（先看这里）

| 事实 | 结论 |
|---|---|
| dev-func（DEV_JOB 管理 + DevJobListener 调度）只装配在 snowy-web-app | 只有 web-app 会调度任务；biz-app 没有调度器 |
| 新增/编辑/执行时按类名 `Class.forName` 加载 | Runner 写在 `snowy-plugin-biz-func`（运行在 biz-app）→ 报"定时任务找不到对应的类" |
| xxl-job | 仓库只带调度中心 `snowy-server/snowy-xxl-job-app`（9004，`/xxl-job-admin`，admin/123456）；web-app、biz-app **均未引入 xxl-job-core、未接执行器**，不能直接"用 xxl-job" |
| 定时逻辑必须跑在 biz-app | ⚠️ **无现成方案**，需自行设计（如自行给 biz-app 接 xxl-job 执行器，或用 Spring `@Scheduled` 并自行处理多实例重复执行；推导，使用前请先确认） |

## 写一个定时任务（标准模板）

```java
/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 * ...（12 行版权头）
 */
package vip.xiaonuo.web.core.timer;   // 示例包：只要最终在 snowy-web-app 类路径上即可，见下方「放置位置」

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.biz.api.BizSupplierApi;
import vip.xiaonuo.common.timer.CommonTimerTaskRunner;

/**
 * 供应商月度对账任务
 *
 * @author 你的名字
 * @date  2026/08/18 10:00
 **/
@Slf4j
@Component
public class BizSupplierMonthlyTimerTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private BizSupplierApi bizSupplierApi;   // 业务逻辑在 biz-app：经 biz-api 接口跨服务调用

    @Override
    public void action(String extJson) {
        // extJson = 界面配置的扩展 JSON 参数（可传开关/日期偏移等）
        log.info("开始执行供应商月度对账");
        bizSupplierApi.doMonthlyReconcile(extJson);
    }
}
```

放置位置（**必须在 snowy-web-app 类路径上**，以下均为推导，使用前请先确认）：

| 情况 | 做法 |
|---|---|
| 任务逻辑本身就在 web-app 已装配的插件里（sys/dev/auth/mobile/client/gen） | Runner 放该插件 `{p}-func` 的 `core/timer/`（平台插件原则上不改，业务一般不走这条） |
| 业务逻辑在 biz-app（常见） | Runner 放 web-app 类路径（如 `snowy-modules/snowy-web-app` 模块内），注入 `BizXxxApi` 跨服务调用；web-app 需加 `snowy-plugin-biz-feign` + `snowy-plugin-biz-api-context`，biz-app 需加 `snowy-plugin-biz-feign-provider`，并补齐 Feign 接口 / FeignProvider / ApiContextBean（出厂均未装配，写法见 plugin-architecture 技能） |

⚠️ 注意：定时线程没有 HTTP 请求上下文，FeignRequestInterceptor 不会透传 token——被调方 provider 里不能依赖当前登录用户（`/feign/**` 在服务端免登录名单，接口本身可调通）。mocker-token 插件的 `@CommonMockerToken` 可为无上下文线程模拟登录，但出厂未装配到任何应用。

写完代码后：**重启 snowy-web-app → 定时任务界面 → 新增 → 选择该 Runner → 填 Cron → 启用**。

## Cron 速查

| 表达式 | 含义 |
|---|---|
| `0 0 2 * * ?` | 每天凌晨 2 点 |
| `0 */5 * * * ?` | 每 5 分钟 |
| `0 0 0 1 * ?` | 每月 1 号零点 |
| `0 0 9-18 * * MON-FRI` | 工作日 9-18 点整点 |

前端有 Cron 组件可视化生成（`snowy-admin-web/src/components/Cron/`）。

## 任务规范

- **幂等**：任务可能重复触发/重叠执行，action 内先判"本次是否已处理"
- **多实例**：CronUtil 是进程内调度、无分布式锁，web-app 部署多个实例时每个实例都会按 Cron 执行一次（推导），需幂等或自行加 Redisson 锁
- **大数据量切片**：分批查处理（每批几百条），别一次 load 全表
- **异常自吞**：单条失败 log.error 继续，不要让整批中断；整体失败要有日志/告警
- **参数走 extJson**：可变配置（天数偏移、开关）放任务配置的扩展 JSON，别写死
- 日志：关键节点 log.info，失败 log.error（看 web-app 应用日志，DEV_JOB 无执行记录表）

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| web-app 侧任务用 `@Scheduled(cron=...)` 注解（Spring 原生） | CommonTimerTaskRunner + DEV_JOB 界面配置（可管理可控） |
| Runner 写在 `snowy-plugin-biz-func`（跑在 biz-app） | Runner 放 web-app 类路径，业务逻辑经 `BizXxxApi` 跨服务调用（否则"找不到对应的类"） |
| 以为"用仓库里的 xxl-job 即可" | 仓库只有 xxl-job 调度中心，业务服务未接执行器；biz-app 侧定时属自行方案（见上方⚠️） |
| web-app 侧再引入 quartz/xxl-job 依赖 | Snowy 自研 timer 已覆盖 |
| 任务里把状态写死 | 可变参数走 extJson |
| 忘记在界面注册任务 | 代码只是 Bean，必须界面新增并启用才调度 |
| 一次性全表加载处理 | 分批 + 幂等 |

## 检查清单

- [ ] Runner 在 snowy-web-app 类路径上（不在 biz-func），@Component，实现 CommonTimerTaskRunner
- [ ] 跨服务调用 biz-app 时，biz 的 feign / feign-provider / api-context 已装配，provider 不依赖登录用户
- [ ] action 用 extJson 接参数，处理幂等与分批；web-app 多实例时已考虑重复执行
- [ ] 界面已配置任务（Cron + 启用）
- [ ] 有执行日志，失败可见

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/timer/CommonTimerTaskRunner.java` | 任务接口定义 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/job/task/DevJobTimerTaskRunner.java` | 官方示例任务 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/job/` | 任务管理模块（DEV_JOB，`Class.forName` 校验类名） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/core/listener/DevJobListener.java` | web-app 启动时加载并调度运行中的任务 |
| `snowy-modules/snowy-web-app/pom.xml` | web-app 装配清单（含 dev-func，决定 Runner 能否被找到） |
| `snowy-server/snowy-xxl-job-app/` | xxl-job 调度中心（业务服务未接执行器） |
| `snowy-admin-web/src/components/Cron/` | Cron 可视化组件 |
