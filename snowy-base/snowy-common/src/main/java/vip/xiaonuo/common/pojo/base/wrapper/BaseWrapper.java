package vip.xiaonuo.common.pojo.base.wrapper;

import java.util.Map;

/**
 * 基础包装接口，
 *
 * @author xuyuxiang
 * @date 2020/7/24 17:18
 */
public interface BaseWrapper<T> {

    /**
     * 具体包装的过程
     *
     * @param beWrappedModel 被包装的原始对象，可以是obj，list，page，PageResult
     * @return 包装后增加的增量集合
     * @author xuyuxiang
     * @date 2020/7/24 17:22
     */
    Map<String, Object> doWrap(T beWrappedModel);

}
