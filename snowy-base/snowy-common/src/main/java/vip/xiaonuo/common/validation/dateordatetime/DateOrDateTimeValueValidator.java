package vip.xiaonuo.common.validation.dateordatetime;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验日期或时间格式 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
 *
 * @author xuyuxiang
 * @date 2020/5/26 14:48
 */
public class DateOrDateTimeValueValidator implements ConstraintValidator<DateOrDateTimeValue, String> {

    @Override
    public boolean isValid(String dateValue, ConstraintValidatorContext context) {
        //为空则放过，因为在此校验之前会加入@NotNull或@NotBlank校验
        if (ObjectUtil.isEmpty(dateValue)) {
            return true;
        }
        //长度不对直接返回
        if (dateValue.length() != DatePattern.NORM_DATE_PATTERN.length() &&
                dateValue.length() != DatePattern.NORM_DATETIME_PATTERN.length()) {
            return false;
        }
        try {
            DateUtil.parseDate(dateValue);
            return true;
        } catch (Exception dateParseException) {
            try {
                DateUtil.parseDateTime(dateValue);
                return true;
            } catch (Exception dateTimeParseException) {
                return false;
            }
        }
    }
}
