package vip.xiaonuo.cache.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import vip.xiaonuo.common.cache.CacheOperator;
import vip.xiaonuo.common.consts.SymbolConstant;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 基于redis的缓存封装
 *
 * @author yubaoshan
 * @date 2020/7/9 10:09
 */
public abstract class AbstractRedisCacheOperator<T> implements CacheOperator<T> {

    private final RedisTemplate<String, T> redisTemplate;

    public AbstractRedisCacheOperator(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, T value) {
        redisTemplate.boundValueOps(getCommonKeyPrefix() + key).set(value);
    }

    @Override
    public void put(String key, T value, Long timeoutSeconds) {
        redisTemplate.boundValueOps(getCommonKeyPrefix() + key).set(value, timeoutSeconds, TimeUnit.SECONDS);
    }

    @Override
    public T get(String key) {
        return redisTemplate.boundValueOps(getCommonKeyPrefix() + key).get();
    }

    @Override
    public void remove(String... key) {
        ArrayList<String> keys = CollectionUtil.toList(key);
        List<String> withPrefixKeys = keys.stream().map(i -> getCommonKeyPrefix() + i).collect(Collectors.toList());
        redisTemplate.delete(withPrefixKeys);
    }

    @Override
    public Collection<String> getAllKeys() {
        Set<String> keys = redisTemplate.keys(getCommonKeyPrefix() + SymbolConstant.ASTERISK);
        if (keys != null) {
            // 去掉缓存key的common prefix前缀
            return keys.stream().map(key -> StrUtil.removePrefix(key, getCommonKeyPrefix())).collect(Collectors.toSet());
        } else {
            return CollectionUtil.newHashSet();
        }
    }

    @Override
    public Collection<T> getAllValues() {
        Set<String> keys = redisTemplate.keys(getCommonKeyPrefix() + SymbolConstant.ASTERISK);
        if (keys != null) {
            return redisTemplate.opsForValue().multiGet(keys);
        } else {
            return CollectionUtil.newArrayList();
        }
    }

    @Override
    public Map<String, T> getAllKeyValues() {
        Collection<String> allKeys = this.getAllKeys();
        HashMap<String, T> results = CollectionUtil.newHashMap();
        for (String key : allKeys) {
            results.put(key, this.get(key));
        }
        return results;
    }
}
