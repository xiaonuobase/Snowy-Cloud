package vip.xiaonuo.sys.modular.timer.enums.exp;

import vip.xiaonuo.common.annotion.ExpEnumType;
import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import vip.xiaonuo.common.factory.ExpEnumCodeFactory;
import vip.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * 定时任务相关枚举
 *
 * @author yubaoshan
 * @date 2020/6/30 18:26
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.TIMER_EXCEPTION_ENUM)
public enum SysTimersExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 该条记录不存在
     */
    NOT_EXISTED(1, "您查询的该条记录不存在"),

    /**
     * 定时任务执行类不存在
     */
    TIMER_NOT_EXISTED(2, "定时任务执行类不存在"),

    /**
     * 检查定时任务启动时候的参数是否传了
     */
    EXE_EMPTY_PARAM(3, "请检查定时器的id，定时器cron表达式，定时任务是否为空！");

    private final Integer code;

    private final String message;

    SysTimersExceptionEnum(int code, String message) {
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
