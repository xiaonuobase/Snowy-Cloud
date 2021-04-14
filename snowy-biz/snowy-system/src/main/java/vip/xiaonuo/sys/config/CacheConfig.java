package vip.xiaonuo.sys.config;

import vip.xiaonuo.cache.AllPermissionCache;
import vip.xiaonuo.cache.MappingCache;
import vip.xiaonuo.cache.ResourceCache;
import vip.xiaonuo.cache.UserCache;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.sys.core.redis.FastJson2JsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;

/**
 * 缓存的配置，默认使用基于内存的缓存，如果分布式部署请更换为redis
 *
 * @author xuyuxiang
 * @date 2020/7/9 11:43
 */
@Configuration
public class CacheConfig {

    /**
     * url资源的缓存，默认不过期
     *
     * @author yubaoshan
     * @date 2020/7/9 11:44
     */
    @Bean
    public ResourceCache resourceCache() {
        return new ResourceCache();
    }

    /**
     * redisTemplate 配置
     *
     * @author dongxiayu
     * @date 2021/1/23 13:55
     */
    @Bean
    public RedisTemplate<String, SysLoginUser> userRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, SysLoginUser> userRedisTemplate = new RedisTemplate<>();
        userRedisTemplate.setConnectionFactory(factory);
        userRedisTemplate.setKeySerializer(new StringRedisSerializer());
        userRedisTemplate.setValueSerializer(new FastJson2JsonRedisSerializer<>(SysLoginUser.class));
        userRedisTemplate.afterPropertiesSet();
        return userRedisTemplate;
    }

    /**
     * redisTemplate 配置
     *
     * @author dongxiayu
     * @date 2021/1/23 13:55
     */
    @Bean
    public RedisTemplate<String, Map<String, Object>> mappingRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Map<String, Object>> mappingRedisTemplate = new RedisTemplate<>();
        mappingRedisTemplate.setConnectionFactory(factory);
        mappingRedisTemplate.setKeySerializer(new StringRedisSerializer());
        mappingRedisTemplate.setValueSerializer(new FastJson2JsonRedisSerializer<>(Map.class));
        mappingRedisTemplate.afterPropertiesSet();
        return mappingRedisTemplate;
    }

    /**
     * 登录用户的缓存，默认过期时间，根据系统sys_config中的常量决定
     *
     * @author yubaoshan
     * @date 2020/7/9 11:44
     */
    @Bean
    public UserCache userCache(RedisTemplate<String, SysLoginUser> userRedisTemplate) {
        return new UserCache(userRedisTemplate);
    }

    /**
     * mapping映射缓存
     *
     * @author xuyuxiang
     * @date 2020/7/24 13:55
     */
    @Bean
    public MappingCache mappingCache(RedisTemplate<String, Map<String, Object>> mappingRedisTemplate) {
        return new MappingCache(mappingRedisTemplate);
    }

    /**
     * allPermissionRedisTemplate 配置
     *
     * @author dongxiayu
     * @date 2021/4/8 13:55
     */
    @Bean
    public RedisTemplate<String, List<String>> allPermissionRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, List<String>> allPermissionRedisTemplate = new RedisTemplate<>();
        allPermissionRedisTemplate.setConnectionFactory(factory);
        allPermissionRedisTemplate.setKeySerializer(new StringRedisSerializer());
        allPermissionRedisTemplate.setValueSerializer(new FastJson2JsonRedisSerializer<>(List.class));
        allPermissionRedisTemplate.afterPropertiesSet();
        return allPermissionRedisTemplate;
    }

    /**
     * allPermission缓存
     *
     * @author dongxiayu
     * @date 2021/4/8 13:55
     */
    @Bean
    public AllPermissionCache allPermissionCache(RedisTemplate<String, List<String>> allPermissionRedisTemplate) {
        return new AllPermissionCache(allPermissionRedisTemplate);
    }

}
