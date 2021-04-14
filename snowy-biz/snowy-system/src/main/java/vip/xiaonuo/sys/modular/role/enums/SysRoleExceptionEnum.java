package vip.xiaonuo.sys.modular.role.enums;

import vip.xiaonuo.common.annotion.ExpEnumType;
import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * 系统角色相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/28 14:47
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_ROLE_EXCEPTION_ENUM)
public enum SysRoleExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST(1, "角色不存在"),

    /**
     * 角色编码重复
     */
    ROLE_CODE_REPEAT(2, "角色编码重复，请检查code参数"),

    /**
     * 角色名称重复
     */
    ROLE_NAME_REPEAT(3, "角色名称重复，请检查name参数");

    private final Integer code;

    private final String message;

    SysRoleExceptionEnum(Integer code, String message) {
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
