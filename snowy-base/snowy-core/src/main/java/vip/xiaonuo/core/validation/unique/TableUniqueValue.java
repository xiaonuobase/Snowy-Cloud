package vip.xiaonuo.core.validation.unique;

import vip.xiaonuo.common.consts.CommonConstant;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证表的的某个字段值是否在是唯一值
 *
 * @author yubaoshan
 * @date 2020/4/14 23:49
 */
@Documented
@Constraint(validatedBy = TableUniqueValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableUniqueValue {

    String message() default "库中存在重复编码，请更换该编码值";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 表名称，例如 sys_user
     */
    String tableName();

    /**
     * 列名称，例如 user_code
     */
    String columnName();

    /**
     * 是否开启状态校验，默认是关闭的
     * <p>
     * 关于为何开启状态校验：
     * <p>
     * 若项目中某个表包含控制逻辑删除的字段，我们在进行唯一值校验的时候要排除这种状态的记录，所以需要用到这个功能
     */
    boolean excludeLogicDeleteItems() default false;

    /**
     * 标识状态的字段名
     */
    String logicDeleteFieldName() default CommonConstant.STATUS;

    /**
     * 逻辑删除的值（默认2是删除），用string是为了更通用
     */
    String logicDeleteValue() default CommonConstant.DEFAULT_LOGIC_DELETE_VALUE;

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        TableUniqueValue[] value();
    }
}
