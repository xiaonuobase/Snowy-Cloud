package vip.xiaonuo.sys.modular.user.enums;

import vip.xiaonuo.common.annotion.ExpEnumType;
import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * 系统用户相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/23 9:32
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_USER_EXCEPTION_ENUM)
public enum SysUserExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1, "用户不存在"),

    /**
     * 账号已存在
     */
    USER_ACCOUNT_REPEAT(2, "账号已存在，请检查account参数"),

    /**
     * 原密码错误
     */
    USER_PWD_ERROR(3, "原密码错误，请检查password参数"),

    /**
     * 新密码与原密码相同
     */
    USER_PWD_REPEAT(4, "新密码与原密码相同，请检查newPassword参数"),

    /**
     * 不能删除超级管理员
     */
    USER_CAN_NOT_DELETE_ADMIN(5, "不能删除超级管理员"),

    /**
     * 不能修改超级管理员状态
     */
    USER_CAN_NOT_UPDATE_ADMIN(6, "不能修改超级管理员状态");

    private final Integer code;

    private final String message;

    SysUserExceptionEnum(Integer code, String message) {
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
