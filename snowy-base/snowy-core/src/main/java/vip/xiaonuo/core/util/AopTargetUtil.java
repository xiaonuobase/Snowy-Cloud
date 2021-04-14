package vip.xiaonuo.core.util;

import cn.hutool.log.Log;
import vip.xiaonuo.common.context.requestno.RequestNoContext;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 获取代理原始对象的工具
 *
 * @author yubaoshan
 * @date 2018/2/21 17:09
 */
public class AopTargetUtil {

    private static final Log log = Log.get();

    /**
     * 获取被代理的对象本身
     *
     * @author yubaoshan
     * @date 2020/6/21 17:02
     */
    public static Object getTarget(Object proxy) {

        // 判断是不是代理对象，如果不是直接返回
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        try {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error(">>> 获取代理对象异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            return null;
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }

}
