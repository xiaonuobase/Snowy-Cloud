package vip.xiaonuo.common.exception;

import vip.xiaonuo.common.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Getter;

/**
 * 认证相关的异常
 * <p>
 * 认证和鉴权的区别：
 * <p>
 * 认证可以证明你能登录系统，认证的过程是校验token的过程
 * 鉴权可以证明你有系统的哪些权限，鉴权的过程是校验角色是否包含某些接口的权限
 *
 * @author xuyuxiang
 * @date 2020/3/12 9:55
 */
@Getter
public class AuthException extends RuntimeException {

    private final Integer code;

    private final String errorMessage;

    public AuthException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

}
