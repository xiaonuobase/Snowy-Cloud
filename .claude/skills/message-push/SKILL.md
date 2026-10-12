---
name: message-push
description: Snowy 消息与推送规范：站内信 DEV_MESSAGE、WebSocket 实时通知、钉钉/企微/飞书推送 DEV_PUSH、前端消息中心联动；微服务版这些能力在 web-app，biz-app 经 Feign 调用。触发场景：1) 业务需要给用户发通知 2) 需要实时推送（WebSocket）3) 对接钉钉/企业微信/飞书机器人 4) biz-app 里发站内信/推送（跨服务）。触发词：消息、通知、站内信、推送、WebSocket、实时、钉钉、企业微信、飞书、DEV_MESSAGE、DEV_PUSH、消息中心、DevMessageApi、DevPushApi、跨服务、Feign。
---

# Snowy 消息与推送规范

## 能力矩阵

| 需求 | 用什么 | 模块（均在 `snowy-plugin-dev-func`，运行在 web-app） |
|---|---|---|
| 站内信（应用内通知） | DevMessageApi 发消息 → 用户点铃铛查看 | `dev/modular/message/`（DEV_MESSAGE 表） |
| 实时弹到页面 | WebSocket（站内信未读数实时通道） | `dev/modular/message/websocket/` |
| 推到钉钉/企微/飞书群机器人 | DevPushApi | `dev/modular/push/`（DEV_PUSH 表存推送记录） |
| 短信/邮件 | 见 sms-mail 技能 | `dev/modular/sms/`、`email/` |

## 跨服务可用性（biz-app 里能不能直接注入）

| 接口 | biz-app 现状（以代码为准） |
|---|---|
| `DevMessageApi` | ✅ 可用：dev-api-context 的 `DevMessaeApiContextBean`（类名原文如此）→ `DevMessageFeign` → web-app 的 `DevMessageFeignProvider` |
| `DevPushApi` | ⚠️ 不可用：dev-api-context 没有对应 ContextBean，dev-feign-provider 也没有 DevPushFeignProvider（`DevPushFeign` 接口存在但无服务端实现）；在 biz-app 注入会缺 Bean（推导）。需要时请自行补齐 Feign 全链路 |
| WebSocket 端点 | 只在 web-app（`/dev/message/ws`），biz-app 没有 |

## 站内信（最常用）

```java
// 跨插件/跨服务调用（biz-app 内经 Feign 调 web-app）
@Resource
private DevMessageApi devMessageApi;      // snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api

// 发给指定用户（消息入库 DEV_MESSAGE + WebSocket 推送未读数给在线用户）
devMessageApi.sendMessage(receiverIdList, "主题");
devMessageApi.sendMessageWithContent(receiverIdList, "主题", "内容");
devMessageApi.sendMessageWithContent(receiverIdList, "分类", "主题", "内容");
```

⚠️ 现状：接口里三参的 `sendMessage(receiverIdList, category, subject)`，实现类 DevMessageProvider 实际把第二个参数当主题、丢弃第三个参数；需要分类请用四参的 `sendMessageWithContent`。

前端：顶栏消息中心（`snowy-admin-web/src/layout/components/message.vue`），未读数角标自动更新。

管理界面：开发工具 → 站内信（可查/批量发）。

## WebSocket（实时通知）

- 服务端：`dev/modular/message/websocket/DevMessageWebSocket.java`，jakarta `@ServerEndpoint("/dev/message/ws")`，用 `?token=` 校验登录；会话保存在 **web-app 进程内存**（ConcurrentHashMap），推送内容是**未读数**，不是消息正文
- 触发时机：`DevMessageServiceImpl` 发站内信后调 `DevMessageWebSocket.sendMessage(receiverIdList)`；静态方法只能在 web-app 进程内调用
- ⚠️ 多实例：没有 Redis 发布订阅，web-app 部署多个实例时只有处理发送请求的那个实例能推给连在它上面的用户（推导），其余用户要等重连/刷新
- 前端链路：`message.vue` 用 `getApiBaseUrl()` + `convertUrl('/dev/message/ws')` 拼地址——开发环境为 `ws://localhost:9000/api/api/webapp/dev/message/ws?token=…`，经 vite 代理（`ws: true`）去掉首个 `/api` 后到网关 9003，再路由到 web-app；生产直连模式把 `API_URL` 的 http 换成 ws

业务一般**不直接操作 WebSocket**——发站内信即自动推送未读数。

## 钉钉/企微/飞书推送

- 配置：存 DEV_CONFIG（`SNOWY_PUSH_*` 键，界面：开发工具 → 系统配置 → 推送配置），默认引擎键 `SNOWY_SYS_DEFAULT_PUSH_ENGINE`；DEV_PUSH 表只存推送记录
- 使用（仅 web-app 内可直接注入，biz-app 见上方⚠️）：

```java
@Resource
private DevPushApi devPushApi;            // pushDynamicText / pushDingTalkText / pushWorkWechatText 等
```

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| 自建消息表 + 轮询 | DEV_MESSAGE 站内信 + WebSocket 自动实时 |
| 每个业务自己开 WebSocket 端点 | 复用 dev 的 websocket 模块（站内信通道） |
| 直接 HTTP 调钉钉 webhook | DevPushApi（可管理/可切换/有记录；biz-app 内需先补 Feign 链路） |
| 以为 WebSocket 多实例自动同步 | 进程内会话，多实例推送需自行方案 |
| 消息内容存 HTML 富文本拼 SQL | 模板 + 参数（参考现有消息类型） |

## 检查清单

- [ ] 站内信走 DevMessageApi（自动入库 + 实时推送未读数）
- [ ] 群机器人推送走 DevPushApi；在 biz-app 里用前已确认 Bean 存在
- [ ] 没有自建轮询接口
- [ ] 消息有类型分类（用户可在前端按类型筛）

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/message/` | 站内信模块（含 websocket/） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/push/` | 推送模块 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevMessageApi.java` | 站内信跨插件接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevPushApi.java` | 推送跨插件接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api-context/src/main/java/vip/xiaonuo/dev/api/context/` | biz-app 可用的 Dev*Api 远程代理清单 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-feign-provider/src/main/java/vip/xiaonuo/dev/feign/provider/` | web-app 侧 Feign 服务端实现清单 |
| `snowy-admin-web/src/layout/components/message.vue` | 前端消息中心（WS 客户端） |
