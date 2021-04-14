package vip.xiaonuo.core.validation.dict;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 检验值是否为字典值，验证sys_dict_data表中有没有相关的字典项
 * <p>
 * 本注解用的时候，一定要加dictType参数，用来表明验证的哪个字典类型中的值
 * <p>
 * dictType值来自数据库中sys_dict_type表的code值
 *
 * @author yubaoshan
 * @date 2020/4/14 23:49
 */
@Documented
@Constraint(validatedBy = DictValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DictValue {

    String message() default "不正确的字典值，请检查数据库中是否录入该字典项";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 字典的类型
     */
    String[] dictType();

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DictValue[] value();
    }
}
