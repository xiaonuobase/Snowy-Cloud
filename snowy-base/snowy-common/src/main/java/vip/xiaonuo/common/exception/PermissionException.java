package vip.xiaonuo.common.exception;

import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Getter;

/**
 * 授权和鉴权异常
 * <p>
 * 认证和鉴权的区别：
 * <p>
 * 认证可以证明你能登录系统，认证的过程是校验token的过程
 * 鉴权可以证明你有系统的哪些权限，鉴权的过程是校验角色是否包含某些接口的权限
 * 也包含当前用户是否有操作该数据的权限
 *
 * @author yubaoshan
 * @date 2019/7/18 22:18
 */
@Getter
public class PermissionException extends RuntimeException {

    private final Integer code;

    private final String errorMessage;

    public PermissionException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

}
