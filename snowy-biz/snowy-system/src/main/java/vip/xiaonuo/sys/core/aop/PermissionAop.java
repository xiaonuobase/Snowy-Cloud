package vip.xiaonuo.sys.core.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.annotion.Permission;
import vip.xiaonuo.common.consts.AopSortConstant;
import vip.xiaonuo.common.consts.SymbolConstant;
import vip.xiaonuo.common.context.login.LoginContextHolder;
import vip.xiaonuo.common.enums.LogicTypeEnum;
import vip.xiaonuo.common.exception.PermissionException;
import vip.xiaonuo.common.exception.enums.PermissionExceptionEnum;
import vip.xiaonuo.core.util.HttpServletUtil;
import vip.xiaonuo.sys.core.log.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 权限过滤Aop切面
 * @author xuyuxiang
 * @date 2020/3/23 17:09
 */
@Aspect
@Order(AopSortConstant.PERMISSION_AOP)
public class PermissionAop {

    private static final Log log = Log.get();

    /**
     * 权限切入点
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:10
     */
    @Pointcut("@annotation(vip.xiaonuo.common.annotion.Permission)")
    private void getPermissionPointCut() {
    }

    /**
     * 执行权限过滤
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:14
     */
    @Before("getPermissionPointCut()")
    public void doPermission(JoinPoint joinPoint) {

        // 如果是超级管理员，直接放过权限校验
        boolean isSuperAdmin = LoginContextHolder.me().isSuperAdmin();
        if (isSuperAdmin) {
            return;
        }

        // 如果不是超级管理员，则开始进行权限校验
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Permission permission = method.getAnnotation(Permission.class);

        // 当前方法需要的角色集合
        String[] requireRoles = permission.value();

        // 逻辑类型
        LogicTypeEnum logicTypeEnum = permission.logicType();

        // 首先校验当前用户有没有 当前请求requestUri的权限
        HttpServletRequest request = HttpServletUtil.getRequest();
        boolean hasUriPermission = LoginContextHolder.me().hasPermission(request.getRequestURI());
        if (!hasUriPermission) {
            this.executeNoPermissionExceptionLog(joinPoint, new PermissionException(PermissionExceptionEnum.NO_PERMISSION));
            throw new PermissionException(PermissionExceptionEnum.NO_PERMISSION);
        }

        // 如果当前接口需要特定的角色权限，则校验参数上的特殊角色当前用户有没
        if (requireRoles.length != 0) {
            boolean hasSpecialRolePermission = true;
            if (LogicTypeEnum.AND.equals(logicTypeEnum)) {
                hasSpecialRolePermission = LoginContextHolder.me().hasAllRole(StrUtil.join(SymbolConstant.COMMA, (Object) requireRoles));
            } else if (LogicTypeEnum.OR.equals(logicTypeEnum)) {
                hasSpecialRolePermission = LoginContextHolder.me().hasAnyRole(StrUtil.join(SymbolConstant.COMMA, (Object) requireRoles));
            } else {
                log.error(">>> permission注解逻辑枚举错误");
            }
            if (!hasSpecialRolePermission) {
                this.executeNoPermissionExceptionLog(joinPoint, new PermissionException(PermissionExceptionEnum.NO_PERMISSION));
                throw new PermissionException(PermissionExceptionEnum.NO_PERMISSION);
            }
        }
    }

    /**
     * 记录无权限异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/24 11:14
     */
    private void executeNoPermissionExceptionLog(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);

        //异步记录日志
        LogManager.me().executeExceptionLog(
                businessLog, LoginContextHolder.me().getSysLoginUserAccount(), joinPoint, exception);
    }

}
