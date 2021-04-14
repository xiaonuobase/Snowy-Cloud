package vip.xiaonuo.common.annotion;

import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;

import java.lang.annotation.*;

/**
 * 标记需要做业务日志的方法
 *
 * @author yubaoshan
 * @date 2017/3/31 12:46
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BusinessLog {

    /**
     * 业务的名称,例如:"修改菜单"
     */
    String title() default "";

    /**
     * 业务操作类型枚举
     */
    LogAnnotionOpTypeEnum opType() default LogAnnotionOpTypeEnum.OTHER;
}
