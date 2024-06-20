/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ThreadLocal工具类
 *
 * @author dongxiayu
 * @date 2020/3/30 15:09
 */
public class CommonThreadLocalUtil {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new HashMap<>(4));

    private CommonThreadLocalUtil() {
    }

    public static Map<String, Object> getThreadLocal(){
        return THREAD_LOCAL.get();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) THREAD_LOCAL.get().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key,T defaultValue) {
        Object value = THREAD_LOCAL.get().get(key);
        return value == null ? defaultValue : (T) value;
    }

    public static void set(String key, Object value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        THREAD_LOCAL.get().putAll(keyValueMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String,T> fetchVarsByPrefix(String prefix) {
        Map<String,T> vars = new HashMap<>();
        if( prefix == null ){
            return vars;
        }
        Map<String, Object> map = THREAD_LOCAL.get();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (null != key && (key.startsWith(prefix))) {
                vars.put(key, (T) entry.getValue());
            }
        }
        return vars;
    }

    @SuppressWarnings("unchecked")
    public static <T> T remove(String key) {
        return (T) (THREAD_LOCAL.get().remove(key));
    }

    public static void clear(String prefix) {
        if( prefix == null ){
            return;
        }
        Map<String, Object> map = THREAD_LOCAL.get();
        Set<String> removeKeys = map.keySet()
                .stream()
                .filter(key -> null != key && (key.startsWith(prefix)))
                .collect(Collectors.toSet());
        removeKeys.forEach(map::remove);
    }

}