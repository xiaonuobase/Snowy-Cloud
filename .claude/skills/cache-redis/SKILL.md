---
name: cache-redis
description: Snowy 缓存使用规范：CommonCacheOperator 统一操作器、Redisson 客户端、缓存 key 常量、数据变更事件联动刷新、alone-redis 独立配置；微服务版各服务共享同一 Redis 与 Sa-Token 会话。触发场景：1) 业务需要读写缓存 2) 改完数据要刷新缓存 3) 需要分布式锁或 Redis 原生结构 4) 跨服务缓存/会话共享问题。触发词：缓存、Redis、Redisson、CommonCacheOperator、CacheConstant、缓存刷新、缓存key、分布式锁、缓存过期、Nacos Redis 配置、共享会话、跨服务缓存。
---

# Snowy 缓存使用规范

## 基础设施

| 组件 | 位置 | 说明 |
|---|---|---|
| `CommonCacheOperator` | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/cache/CommonCacheOperator.java` | **统一缓存操作器**（封装 Redisson Bucket，key 自动加前缀 `Cache:`） |
| `RedissonConfig` | 每个服务各一份：`snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/RedissonConfig.java`、`snowy-modules/snowy-biz-app/src/main/java/vip/xiaonuo/biz/core/config/RedissonConfig.java`、`snowy-server/snowy-gateway-app/src/main/java/vip/xiaonuo/gateway/config/RedissonConfig.java` | Redisson 客户端装配（redissonClient + Sa-Token 用的 aloneRedissonClient） |
| `CacheConstant` | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/CacheConstant.java` | 框架级缓存 key 常量 |
| 数据变更事件 | `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/listener/CommonDataChangeEventCenter.java` | 增删改后广播事件，**本服务内**各插件监听刷新自己的缓存 |

连接：不在本地配置文件，在 **Nacos** 各服务配置 `snowy-{应用名}-dev.yaml` 的 `spring.data.redis` 段（127.0.0.1:6379，database 1；源文件在 `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/`）；Sa-Token 用 `sa-token.alone-redis` 独立配置（同实例同库）。

**微服务共享**：web-app、biz-app、网关连的是同一 Redis 库——
- Sa-Token 会话共享：网关与 biz-app 都从 Redis 里的 Token 会话读 `loginUser` 做鉴权
- `CommonCacheOperator` 的 `Cache:` 命名空间也是全服务共享：biz-app 写的 key，web-app 读得到，反之亦然

## CommonCacheOperator API（业务缓存首选）

```java
@Resource
private CommonCacheOperator commonCacheOperator;

commonCacheOperator.put(key, value);                       // 永久（逻辑过期由业务控制）
commonCacheOperator.put(key, value, 60);                   // 60 秒过期
Object v = commonCacheOperator.get(key);                   // 取（无则 null）
commonCacheOperator.remove(key1, key2);                    // 删一个或多个
commonCacheOperator.removeBatch("prefix:*");               // 按模式批量删
commonCacheOperator.getAllKeys();                          // 全部 key（调试用）
```

- key 命名：`{业务}:{对象}:{标识}` 小写冒号分层，如 `biz:supplier:count`；框架已占用的前缀见 CacheConstant（`permission-resource`、`auth-b-permission-list:` 等，**不要冲突**）；多服务共用同一命名空间，**业务前缀必须带上**，防止与其他服务撞 key
- 值直接存对象（Redisson 编解码），不需要先 JSON 序列化

## 需要原生 Redis 结构/锁时：RedissonClient

```java
@Resource
private RedissonClient redissonClient;

// 分布式锁
RLock lock = redissonClient.getLock("biz:order:lock:" + orderId);
try {
    if(lock.tryLock(3, 10, TimeUnit.SECONDS)) {           // 等3秒，持10秒
        // 临界区
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
} finally {
    if(lock.isHeldByCurrentThread()) {
        lock.unlock();                                     // 必须 finally 且校验持有者
    }
}

// 其他结构
RMap<String, Object> map = redissonClient.getMap("biz:xxx:map");
RAtomicLong counter = redissonClient.getAtomicLong("biz:xxx:counter");
```

## 数据变更事件（缓存联动的标准姿势）

改了被缓存的数据源后，广播事件让监听方刷新：

```java
// 写操作后发事件（在 Service 的 add/edit/delete 里；参考 BizGroupServiceImpl）
CommonDataChangeEventCenter.doAddWithData(BizDataTypeEnum.XXX.getValue(), JSONUtil.createArray().put(entity));     // 新增
CommonDataChangeEventCenter.doUpdateWithData(BizDataTypeEnum.XXX.getValue(), JSONUtil.createArray().put(entity));  // 更新
CommonDataChangeEventCenter.doDeleteWithDataIdList(BizDataTypeEnum.XXX.getValue(), idList);                       // 删除

// 各插件在 core/listener/ 实现 CommonDataChangeListener（参考 BizDataChangeListener）
```

典型消费方：biz-app 内的 BizDataChangeListener（机构变更清机构缓存）；web-app 内 sys 插件的 SysDataChangeListener。

⚠️ **事件不跨服务**：`CommonDataChangeEventCenter` 是进程内静态侦听器列表，只通知**本服务 JVM 内**注册的侦听器——biz-app 里发的事件 web-app 收不到，多实例之间也不互通。跨服务的缓存联动只能靠共享 Redis 直接删 key 或自行通知（推导，使用前请先确认）。

⚠️ 现状（推导，未运行验证）：
- web-app 里 `GlobalConfigure` 与 `vip.xiaonuo.core.config.DataChangeListenerConfiguration` 都调用了 `registerListenerList`，侦听器可能被注册两次、每个事件触发两遍
- web-app 与 biz-app 的 `ResourceCollectListener` 启动时都**整体覆盖**同一个共享 key `permission-resource`，后启动的服务会覆盖先启动服务的接口清单（可能导致角色授权里的接口权限列表不全）

## 使用原则

| 场景 | 方案 |
|---|---|
| 普通 key-value 业务缓存 | CommonCacheOperator（+ 过期秒数） |
| 计数器/防重/限流 | RedissonClient 的 RAtomicLong / RLock |
| 改了共享基础数据（字典/配置/资源） | 本服务内发 CommonDataChangeEventCenter 事件；需要别的服务感知时直接删共享 Redis key |
| 登录用户信息/权限 | 框架已管（Sa-Token TokenSession，全服务共享），业务不要碰 |

## 常见错误正误对照

| ❌ | ✅ |
|---|---|
| `stringRedisTemplate.opsForValue()...` | `commonCacheOperator.put/get`（项目无 StringRedisTemplate 装配） |
| 直接注入 `RedisTemplate` | Redisson 体系（CommonCacheOperator/RedissonClient） |
| 改字典后不广播事件 | `CommonDataChangeEventCenter.doUpdateWithData(...)` |
| 以为 biz-app 发的事件 web-app 能收到 | 事件只在本服务 JVM 内生效，跨服务靠共享 Redis |
| 分布式锁 unlock 不判持有者 | `if(lock.isHeldByCurrentThread()) lock.unlock()` 在 finally |
| key 大写/无分层 | 小写冒号分层 `{业务}:{对象}:{id}` |
| 缓存业务对象手动 JSON.toJSONString | 直接存对象 |

## 检查清单

- [ ] 用的 CommonCacheOperator 而非自造 Redis 封装
- [ ] key 有业务前缀分层，不与 CacheConstant 及其他服务的 key 冲突
- [ ] 有过期时间的缓存设置了秒数
- [ ] 数据变更后发了 DataChangeEvent（涉及共享数据时），并清楚它不跨服务
- [ ] 分布式锁在 finally 释放且校验持有者
- [ ] 本地 Redis 已启动（127.0.0.1:6379 database 1，以 Nacos 配置为准）
- [ ] 本地多个 Snowy 项目没有共用同一 Redis 库号（token 名、superAdmin ID 相同会串会话，登录报 ClassCastException）

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/cache/CommonCacheOperator.java` | 操作器源码 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/consts/CacheConstant.java` | 框架 key 常量 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/listener/CommonDataChangeEventCenter.java` | 事件中心 |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/core/listener/BizDataChangeListener.java` | 监听实现范本 |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/group/service/impl/BizGroupServiceImpl.java` | 发事件写法范本 |
| `snowy-modules/snowy-web-app/src/main/java/vip/xiaonuo/web/core/config/RedissonConfig.java` | Redisson 装配（biz-app、网关各有一份同构配置） |
| `snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/dev/snowy-web-app-dev.yaml` | Redis / alone-redis 连接配置（其他服务同目录） |
