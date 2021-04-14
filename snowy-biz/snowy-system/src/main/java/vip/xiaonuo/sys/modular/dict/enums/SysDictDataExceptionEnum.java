package vip.xiaonuo.sys.modular.dict.enums;

import vip.xiaonuo.common.annotion.ExpEnumType;
import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * 系统字典值相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:47
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_DICT_DATA_EXCEPTION_ENUM)
public enum SysDictDataExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 字典值不存在
     */
    DICT_DATA_NOT_EXIST(1, "字典值不存在"),

    /**
     * 字典值编码重复
     */
    DICT_DATA_CODE_REPEAT(2, "字典值编码重复，请检查code参数");

    private final Integer code;

    private final String message;

    SysDictDataExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
