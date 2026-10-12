---
name: sms-mail
description: Snowy 短信与邮件规范：sms4j 多供应商短信、DEV_SMS 发送记录、DevEmailApi 邮件（CommonEmailUtil 仅做邮箱校验）、SMTP 配置、验证码发送流程与 auth 联动；微服务版能力在 web-app，biz-app 经 Feign 调用。触发场景：1) 业务需要发短信/邮件通知 2) 手机/邮箱验证码登录相关 3) 配置短信供应商或 SMTP 4) biz-app 里发短信/邮件（跨服务）。触发词：短信、SMS、sms4j、邮件、email、SMTP、验证码、通知发送、DEV_SMS、DEV_EMAIL、DevSmsApi、DevEmailApi、跨服务、Feign。
---

# Snowy 短信与邮件规范

## 短信（sms4j）

- 引擎：org.dromara.sms4j；Snowy 封装了**阿里云、腾讯云**两家（`DevSmsAliyunUtil` / `DevSmsTencentUtil`），默认引擎由 DEV_CONFIG 的 `SNOWY_SYS_DEFAULT_SMS_ENGINE` 决定
- 模块：`snowy-plugin-dev-func` 的 `modular/sms/`（**运行在 web-app**）；管理界面：开发工具 → 短信管理（DEV_SMS 表存发送记录）
- 供应商配置：存 DEV_CONFIG（`SNOWY_SMS_*` 键，界面：开发工具 → 系统配置 → 短信配置）；Nacos `snowy-web-app-dev.yaml` 里只有 `sms-oa` 线程池段

```java
// 业务发短信（web-app 内为本地实现；biz-app 内为 DevSmsApiContextBean → DevSmsFeign → web-app）
@Resource
private DevSmsApi devSmsApi;        // snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api

devSmsApi.sendDynamicSms("手机号", "模板编码或id", paramMap);   // 按默认引擎发送，paramMap 为 JSONObject
devSmsApi.sendSmsAliyun("手机号", "签名", "模板编码", "{\"code\":\"123456\"}");
```

- 不建议业务直接用 sms4j 原生 `SmsFactory`：Snowy 在 DevSms*Util 里按 DEV_CONFIG 动态注册供应商；**biz-app 已在 pom 中排除 sms4j 依赖**，无法使用原生 API
- ⚠️ 注意：`sendDynamicSms` 的 `paramMap`（JSONObject）经 Feign 以 `@RequestParam` 传递，biz-app 侧调用能否正确传参**未验证，使用前请先确认**

发送后记录自动落 DEV_SMS（含状态），短信管理界面可查。

## 邮件

```java
// 发邮件走 DevEmailApi（snowy-plugin-dev-api）；biz-app 内为 DevEmailApiContextBean → DevEmailFeign → web-app
@Resource
private DevEmailApi devEmailApi;

devEmailApi.sendDynamicTxtEmail("收件人,逗号分隔", "主题", "内容");     // 按默认邮件引擎发送
devEmailApi.sendDynamicHtmlEmail("收件人", "主题", "<h1>HTML</h1>");
// 本地 SMTP / 阿里云 / 腾讯云专用方法见 DevEmailApi 定义
```

- `CommonEmailUtil`（snowy-common）只有 `isNotEmail` / `validEmail` 邮箱格式校验，**不负责发送**
- SMTP 配置：存 DEV_CONFIG（`SNOWY_EMAIL_LOCAL_FROM / _PASSWORD / _SMTP_HOST / _SMTP_PORT / _SSL_ENABLE` 等键，界面：开发工具 → 系统配置 → 邮件配置），默认引擎键 `SNOWY_SYS_DEFAULT_EMAIL_ENGINE`
- 发送记录：DEV_EMAIL 表（开发工具 → 邮件管理）
- ⚠️ 注意：带附件的 `sendTextEmailLocal` / `sendHtmlEmailLocal`（参数含 `List<File>`、`Map<String, InputStream>`）经 Feign 以 `@RequestParam` 传递，跨服务无法真正传文件（推导，使用前请先确认）；biz-app 里优先用 Dynamic 文本/HTML 方法

## 验证码流程（与 auth 联动）

```
1. 前端 /auth/b/getPhoneValidCode（或邮箱版；经网关为 /api/webapp/auth/b/...，已在免登录名单）→ web-app 生成验证码
2. 存 Redis（CommonCacheOperator，带过期时间，键前缀 auth-validCode: + 手机号/邮箱 + 请求号）
3. 调 devSmsApi.sendDynamicSms / devEmailApi.sendDynamicHtmlEmail 发送（auth 与 dev 同在 web-app，为本地调用）
4. 用户提交登录/绑定 → 后端从缓存取码比对（一次性，验证即删）
5. 错误次数限制防爆破
```

参考实现：`snowy-plugin-auth-func` 的 `modular/login/` 验证码相关方法（AuthController / AuthClientController / AuthServiceImpl）。

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| 手写 HttpClient 调短信商 API | DevSmsApi（底层 sms4j） |
| biz-app 里引 sms4j 原生发短信 | biz-app 已排除 sms4j，走 DevSmsApi（Feign） |
| 验证码存数据库表 | Redis（CommonCacheOperator + 过期） |
| 邮件密码/SMTP 明文写代码里 | 系统配置 → 邮件配置（DEV_CONFIG） |
| 用 CommonEmailUtil 发邮件 | 它只做邮箱格式校验，发送走 DevEmailApi |
| 发送失败用户端报 500 | 捕获后 CommonException("短信发送失败，请稍后再试")，记录落 DEV_SMS |

## 检查清单

- [ ] 走 DevSmsApi / DevEmailApi，未自造 HTTP 客户端
- [ ] 验证码有过期时间与一次性消费
- [ ] 敏感配置在系统配置（DEV_CONFIG），不在代码
- [ ] 发送结果可追溯（DEV_SMS / DEV_EMAIL 记录）
- [ ] biz-app 跨服务调用避开附件类邮件方法，复杂参数已确认能经 Feign 正确传递

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/sms/` | 短信模块全套 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/email/` | 邮件模块全套（util/DevEmailLocalUtil 读 SMTP 配置） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevSmsApi.java` | 跨插件短信接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevEmailApi.java` | 跨插件邮件接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api-context/src/main/java/vip/xiaonuo/dev/api/context/` | biz-app 侧远程代理（DevSmsApiContextBean / DevEmailApiContextBean） |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/` | 验证码登录流程 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonEmailUtil.java` | 邮箱格式校验 |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml` | sms-oa 线程池配置段 |
