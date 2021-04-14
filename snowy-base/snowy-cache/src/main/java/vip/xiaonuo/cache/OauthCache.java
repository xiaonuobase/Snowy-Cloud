package vip.xiaonuo.cache;

import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import me.zhyd.oauth.cache.AuthCacheConfig;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Oauth登录的缓存
 *
 * @author xuyuxiang
 * @date 2020/7/28 17:42
 **/
@Component
public class OauthCache implements AuthStateCache {

    private final TimedCache<String, String> timedCache = new TimedCache<>(AuthCacheConfig.timeout);

    @Override
    public void cache(String key, String value) {
        timedCache.put(key, value, AuthCacheConfig.timeout);
    }

    @Override
    public void cache(String key, String value, long timeoutSeconds) {
        timedCache.put(key, value, AuthCacheConfig.timeout);
    }

    @Override
    public String get(String key) {
        return timedCache.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        Iterator<CacheObj<String, String>> cacheObjIterator = timedCache.cacheObjIterator();
        while (cacheObjIterator.hasNext()) {
            String temKey = cacheObjIterator.next().getKey();
            if (temKey.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
