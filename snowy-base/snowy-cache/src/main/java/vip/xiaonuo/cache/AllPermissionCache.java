package vip.xiaonuo.cache;

import vip.xiaonuo.cache.base.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 所有权限缓存
 * @author : dongxiayu
 * @date : 2021/4/8 16:06
 */
public class AllPermissionCache extends AbstractRedisCacheOperator<List<String>> {

    /**
     * 登录用户缓存前缀
     */
    public static final String ALL_PERMISSION_CACHE_PREFIX = "All_PERMISSION_CACHE";

    public AllPermissionCache(RedisTemplate<String, List<String>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return ALL_PERMISSION_CACHE_PREFIX;
    }
}
