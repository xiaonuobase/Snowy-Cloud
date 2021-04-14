package vip.xiaonuo.common.context.param;

import cn.hutool.core.lang.Dict;

/**
 * 临时保存http请求的参数
 * <p>
 * 可以保存@RequestBody的可以保存parameter方式传参的
 *
 * @author xuyuxiang
 * @date 2020/8/20
 */
public class RequestParamContext {

    private static final ThreadLocal<Dict> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 保存请求参数
     *
     * @author yubaoshan
     * @date 2020/6/21 20:17
     */
    public static void set(Dict requestParam) {
        CONTEXT_HOLDER.set(requestParam);
    }

    /**
     * 保存请求参数
     *
     * @author yubaoshan
     * @date 2020/6/21 20:17
     */
    public static void setObject(Object requestParam) {

        if (requestParam == null) {
            return;
        }

        if (requestParam instanceof Dict) {
            CONTEXT_HOLDER.set((Dict) requestParam);
        } else {
            CONTEXT_HOLDER.set(Dict.parse(requestParam));
        }
    }

    /**
     * 获取请求参数
     *
     * @author yubaoshan
     * @date 2020/6/21 20:17
     */
    public static Dict get() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除请求参数
     *
     * @author yubaoshan
     * @date 2020/6/21 20:17
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
