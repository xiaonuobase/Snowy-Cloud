package vip.xiaonuo.common.annotion;

import vip.xiaonuo.common.enums.LogicTypeEnum;

import java.lang.annotation.*;

/**
 * 权限注解，用于检查权限
 * 使用方式：@Permission表示检查是否有权限访问该资源
 *
 * @author xuyuxiang
 * @date 2020/3/11 14:44
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Permission {

    /**
     * 加上此注解表示需要有该资源url的才可以访问, 默认值为空，即该url，如果设置了值，则表示有该角色才可以访问
     */
    String[] value() default {};

    /**
     * 逻辑枚举，默认或
     */
    LogicTypeEnum logicType() default LogicTypeEnum.OR;
}
