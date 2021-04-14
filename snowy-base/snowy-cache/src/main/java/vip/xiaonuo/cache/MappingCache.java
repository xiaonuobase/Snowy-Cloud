package vip.xiaonuo.cache;

import vip.xiaonuo.cache.base.AbstractRedisCacheOperator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * mapping是映射，指业务id和业务名称的映射，或字典code和字典值的映射
 * <p>
 * mapping的含义：
 * 指对接口响应字段转化（或完善）过程
 * <p>
 * 为什么要映射：
 * 一般在查询类的方法，响应结果如果需要返回详细的一些名称信息
 * 则需要通过left join关联对应明细表或字典表，通过id或者code查到对应的中文名称，这样做效率不高
 * <p>
 * 结论：
 * 利用缓存，将常用的字典或者业务id映射，保存起来
 * 一方面保证了查询速度，一方面简化了代码开发，不用写一些left join之类的sql
 *
 * @author xuyuxiang
 * @date 2020/7/24 11:59
 */
public class MappingCache extends AbstractRedisCacheOperator<Map<String, Object>> {

    /**
     * 缓存的前缀标识
     */
    public static final String MAPPING_CACHE_PREFIX = "MAPPINGS_";

    public MappingCache(RedisTemplate<String, Map<String, Object>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return MAPPING_CACHE_PREFIX;
    }

}
