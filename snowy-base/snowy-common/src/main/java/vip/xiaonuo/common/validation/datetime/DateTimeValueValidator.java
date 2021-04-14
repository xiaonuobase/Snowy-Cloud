package vip.xiaonuo.common.validation.datetime;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验日期时间格式 yyyy-MM-dd HH:mm:ss
 *
 * @author xuyuxiang
 * @date 2020/5/26 14:48
 */
public class DateTimeValueValidator implements ConstraintValidator<DateTimeValue, String> {

    @Override
    public boolean isValid(String dateTimeValue, ConstraintValidatorContext context) {
        //为空则放过，因为在此校验之前会加入@NotNull或@NotBlank校验
        if (ObjectUtil.isEmpty(dateTimeValue)) {
            return true;
        }
        //长度不对直接返回
        if (dateTimeValue.length() != DatePattern.NORM_DATETIME_PATTERN.length()) {
            return false;
        }
        try {
            DateUtil.parseDateTime(dateTimeValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
