package vip.xiaonuo.sys.modular.org.enums;

import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;
import vip.xiaonuo.common.annotion.ExpEnumType;

/**
 * 系统组织机构相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/26 10:12
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_ORG_EXCEPTION_ENUM)
public enum SysOrgExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 组织机构不存在
     */
    ORG_NOT_EXIST(1, "组织机构不存在"),

    /**
     * 组织机构编码重复
     */
    ORG_CODE_REPEAT(2, "组织机构编码重复，请检查code参数"),

    /**
     * 组织机构名称重复
     */
    ORG_NAME_REPEAT(3, "组织机构名称重复，请检查name参数"),

    /**
     * 该机构下有员工
     */
    ORG_CANNOT_DELETE(4, "该机构下有员工，无法删除"),

    /**
     * 父节点不能和本节点一致，请重新选择父节点
     */
    ID_CANT_EQ_PID(5, "父节点不能和本节点一致，请重新选择父节点"),

    /**
     * 父节点不能为本节点的子节点，请重新选择父节点
     */
    PID_CANT_EQ_CHILD_ID(6, "父节点不能为本节点的子节点，请重新选择父节点");

    private final Integer code;

    private final String message;

    SysOrgExceptionEnum(Integer code, String message) {
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
