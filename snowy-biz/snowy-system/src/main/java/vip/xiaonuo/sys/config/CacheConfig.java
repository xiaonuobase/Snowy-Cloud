/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vip.xiaonuo.cache.AllPermissionCache;
import vip.xiaonuo.cache.MappingCache;
import vip.xiaonuo.cache.ResourceCache;
import vip.xiaonuo.cache.UserCache;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.sys.core.redis.FastJson2JsonRedisSerializer;

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
