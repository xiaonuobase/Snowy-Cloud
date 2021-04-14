package vip.xiaonuo.common.validation.mothordatetime;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验日期格式 yyyy-MM 或 yyyy-MM-dd HH:mm:ss
 *
 * @author xuyuxiang
 * @date 2020/5/26 14:48
 */
@Documented
@Constraint(validatedBy = MonthOrDateTimeValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MonthOrDateTimeValue {

    String message() default "日期格式不正确，正确格式应为yyyy-MM或yyyy-MM-dd HH:mm:ss";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MonthOrDateTimeValue[] value();
    }
}
