package vip.xiaonuo.generate.modular.enums;

import vip.xiaonuo.common.annotion.ExpEnumType;
import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * 代码生成基础配置相关异常枚举
 *
 * @author yubaoshan
 * @date 2020年12月16日21:21:14
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_APP_EXCEPTION_ENUM)
public enum CodeGenerateExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 代码生成基础配置不存在
     */
    CODE_GEN_NOT_EXIST(1, "代码生成基础配置不存在"),

    /**
     * 本地生成代码输出路径错误
     */
    CODE_GEN_NOT_PATH(2,"本地生成代码输出路径错误"),

    /**
     * 请检查此数据表中主键的定义
     */
    CODE_GEN_TABLE_NOT_PRI(3,"请检查此数据表中主键的定义");

    private final Integer code;

    private final String message;

    CodeGenerateExceptionEnum(Integer code, String message) {
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
