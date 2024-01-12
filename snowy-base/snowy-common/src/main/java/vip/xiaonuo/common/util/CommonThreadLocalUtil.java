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

import java.util.*;

/**
 * ThreadLocal工具类
 *
 * @author dongxiayu
 * @date 2020/3/30 15:09
 */
public class CommonThreadLocalUtil<T> {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(4);
        }
    };


    public static Map<String, Object> getThreadLocal(){
        return THREAD_LOCAL.get();
    }

    public static <T> T get(String key) {
        Map map = (Map) THREAD_LOCAL.get();
        return (T)map.get(key);
    }

    public static <T> T get(String key,T defaultValue) {
        Map map = (Map) THREAD_LOCAL.get();
        return (T)map.get(key) == null ? defaultValue : (T)map.get(key);
    }

    public static void set(String key, Object value) {
        Map map = (Map) THREAD_LOCAL.get();
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map map = (Map) THREAD_LOCAL.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static <T> Map<String,T> fetchVarsByPrefix(String prefix) {
        Map<String,T> vars = new HashMap<>();
        if( prefix == null ){
            return vars;
        }
        Map map = (Map) THREAD_LOCAL.get();
        Set<Map.Entry> set = map.entrySet();

        for( Map.Entry entry : set){
            Object key = entry.getKey();
            if( key instanceof String ){
                if( ((String) key).startsWith(prefix) ){
                    vars.put((String)key,(T)entry.getValue());
                }
            }
        }
        return vars;
    }

    public static <T> T remove(String key) {
        Map map = (Map) THREAD_LOCAL.get();
        return (T)map.remove(key);
    }

    public static void clear(String prefix) {
        if( prefix == null ){
            return;
        }
        Map map = (Map) THREAD_LOCAL.get();
        Set<Map.Entry> set = map.entrySet();
        List<String> removeKeys = new ArrayList<>();

        for( Map.Entry entry : set ){
            Object key = entry.getKey();
            if( key instanceof String ){
                if( ((String) key).startsWith(prefix) ){
                    removeKeys.add((String)key);
                }
            }
        }
        for( String key : removeKeys ){
            map.remove(key);
        }
    }

}