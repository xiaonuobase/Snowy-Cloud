---
name: file-oss-management
description: Snowy 文件上传下载与对象存储规范：DevFile 模块多后端上传 API、本地/OSS/COS/MinIO 配置、前端 XnUpload/CropUpload 组件联动；微服务版文件能力在 web-app，biz-app 经 Feign 调用。触发场景：1) 业务需要上传文件/图片/附件 2) 下载或预览文件 3) 配置或切换文件存储后端 4) biz-app 后端要存文件、上传经网关 404。触发词：文件上传、上传、下载、OSS、MinIO、阿里云、腾讯云、对象存储、XnUpload、附件、文件预览、DEV_FILE、file-url、DevFileApi、Feign 上传、跨服务文件。
---

# Snowy 文件管理规范

## 架构

- 存储：x-file-storage 框架，多后端可切换（本地磁盘 / 阿里云 OSS / 腾讯云 COS / MinIO / FTP / rustfs）
- 后端模块：`snowy-plugin-dev-func` 的 `modular/file/`（controller/entity/mapper/param/service/util/provider），元数据落 `DEV_FILE` 表；**dev-func 只装配在 snowy-web-app**，文件接口只在 web-app 提供
- biz-app：启动模块 pom 对 biz-func、dev-feign、dev-api-context 都**排除了 x-file-storage-spring**，本地不能直接操作存储；后端要存文件只能注入 `DevFileApi`（由 dev-api-context 的 DevFileApiContextBean 经 Feign 调 web-app）
- 前端组件：`XnUpload`（通用上传）、`CropUpload`（头像裁剪）、`XnFilePreview`（预览）
- 管理界面：开发工具 → 文件管理

## 上传 API（DevFileController，路径前缀 /dev/file/，经网关为 /api/webapp/dev/file/）

按**后端** × **返回值** 组合成对：`upload{Backend}Return{Id|Url}`：

| 方法 | 返回 |
|---|---|
| `uploadDynamicReturnId / uploadDynamicReturnUrl` | 按系统默认存储（DEV 配置的动态后端），返回文件 id / 可访问 URL |
| `uploadImageDynamicReturnId / Url` | 图片专用（校验图片格式） |
| `uploadDocumentDynamicReturnId / Url` | 文档专用 |
| `uploadLocalReturnId / Url` | 强制本地存储 |
| `uploadAliyunReturnId / Url`、`uploadTencentReturnId / Url`、`uploadMinioReturnId / Url`、`uploadFtpReturnId / Url` | 指定后端 |

参数一律 `@RequestPart("file") MultipartFile file`。

**返回 id 还是 Url**：需要后续管理/鉴权（可撤销、可追踪）→ 存 id（String 落业务表）；仅需展示 → Url。**推荐业务表存 id**（形如 `avatar`、`imageId` 字段），展示时经文件接口换 URL，可防直链失效。

**下载地址怎么拼**：DevFileServiceImpl 用 Nacos `snowy-web-app-dev.yaml` 的 `snowy.config.common.file-url`（出厂 `http://localhost:9003/api/webapp`，即走网关）拼 `/dev/file/download?id=…&sign=…` 或 `/dev/file/authDownload?id=…&token=`；部署时要改成真实网关地址，否则返回的 URL 不可用。

## 业务代码用法

```java
// 一般不在后端手写上传逻辑——前端走 XnUpload 组件直接调 /dev/file/upload*，拿到 id 存业务表
// 后端需要主动上传/查文件时（biz-app 内即跨服务）：
@Resource
private DevFileApi devFileApi;      // biz-app 中实际是 DevFileApiContextBean → DevFileFeign → web-app
```

⚠️ 注意：`DevFileFeign` 的上传方法以 `@RequestParam` 传 `MultipartFile`，未声明 multipart、项目也未配置 Feign 表单编码器——biz-app 后端经 `DevFileApi` 上传文件能否成功**未验证，使用前请先确认**；查询类方法（getFileInfoById / getFileListByIds / getFileBytes）不受此影响。

下载/预览：`/dev/file/download`（免登录，网关已放行 `/api/webapp/dev/file/download`）、`/dev/file/authDownload`（鉴权下载，void + HttpServletResponse，见 DevFileController 尾部方法）。

## 前端联动（详见 frontend-pc 技能）

```vue
<XnUpload
    :upload-number="1"
    v-model:value="formData.imageId"       <!-- 业务字段存文件 id -->
/>
```

- XnUpload 的上传走 vite 代理：`getApiBaseUrl()`（开发环境 `/api`）+ `convertUrl('/dev/file/…')` → 代理去掉 `/api` → 网关 9003 → web-app
- ⚠️ 现状：组件只对**默认**上传/下载地址做了 `convertUrl`；传入自定义 `uploadDynamicReturnUrlApi` 等属性时不会加网关前缀。出厂 `views/biz/notice/form.vue`、`views/dev/slideshow/form.vue` 传的是裸路径 `/dev/file/uploadImageDynamicReturnUrl`（推导：经网关会 404，使用前请先确认）；新写时传 `convertUrl('/dev/file/xxx')`

## 存储后端配置

- 本地/各云的连接参数：存 DEV_CONFIG（`SNOWY_FILE_*` 键，界面：开发工具 → 系统配置 → 文件配置），默认引擎键 `SNOWY_SYS_DEFAULT_FILE_ENGINE`；Nacos 里没有存储连接参数
- 切换默认后端：系统配置界面修改；改配置后无需重启（动态）
- 上传大小限制：Nacos 各服务配置的 `spring.servlet.multipart.max-file-size: 100MB`（web-app、biz-app 都有）
- 本地存储目录在 web-app 所在机器（`SNOWY_FILE_LOCAL_FOLDER_FOR_WINDOWS / _FOR_UNIX`），web-app 多实例部署时本地存储不共享（推导），宜用对象存储

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| 业务 Controller 自己写 MultipartFile 落盘逻辑 | 走 dev/file 上传接口（本地存储或云） |
| 业务表存完整外链 URL（换桶全断） | 存文件 id，展示时换 URL |
| 前端手写 axios FormData 上传 | XnUpload / CropUpload 组件 |
| 下载接口手写 response 输出流 | `/dev/file/download` 或 CommonDownloadUtil |
| 把 DEV_FILE 当业务附件表用 | 业务附件关系建自己的表（存 fileId 外键） |
| biz-app 里引入 x-file-storage 自己存文件 | 前端直传 web-app 的 /dev/file 接口；后端走 DevFileApi（跨服务） |
| XnUpload 自定义上传地址传裸路径 | 传 `convertUrl('/dev/file/xxx')`，带上网关前缀 |

## 检查清单

- [ ] 上传走 dev/file 标准接口，未自造落盘
- [ ] 业务表存的是文件 id（varchar）
- [ ] 前端用 XnUpload（头像用 CropUpload），自定义上传地址已做 convertUrl
- [ ] 涉及私有文件的下载用 authDownload（鉴权）
- [ ] 上传格式/大小有约束（图片接口自动校验图片）
- [ ] Nacos 的 `snowy.config.common.file-url` 指向实际可访问的网关地址

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/file/controller/DevFileController.java` | 全部上传/下载 API |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-func/src/main/java/vip/xiaonuo/dev/modular/file/service/impl/DevFileServiceImpl.java` | 存储逻辑（含 file-url 拼下载地址） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api/src/main/java/vip/xiaonuo/dev/api/DevFileApi.java` | 跨插件/跨服务文件接口 |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-feign/src/main/java/vip/xiaonuo/dev/feign/DevFileFeign.java` | 文件 Feign 接口（biz-app 调用链） |
| `snowy-plugin/snowy-plugin-dev/snowy-plugin-dev-api-context/src/main/java/vip/xiaonuo/dev/api/context/DevFileApiContextBean.java` | biz-app 侧 DevFileApi 远程代理 |
| `snowy-modules/snowy-biz-app/pom.xml` | biz-app 排除 x-file-storage-spring 的位置 |
| `snowy-admin-web/src/components/XnUpload/` | 上传组件（含 README） |
| `snowy-admin-web/src/components/CropUpload/` | 裁剪上传组件 |
