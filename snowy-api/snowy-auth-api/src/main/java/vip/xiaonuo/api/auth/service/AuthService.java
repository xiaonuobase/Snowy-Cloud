package vip.xiaonuo.api.auth.service;

import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证相关service
 *
 * @author xuyuxiang
 * @date 2020/3/11 14:14
 */
public interface AuthService {

    /**
     * 账号密码登录
     *
     * @param account  账号
     * @param password 密码
     * @return token
     * @author xuyuxiang
     * @date 2020/3/11 15:57
     */
    String login(String account, String password);

    /**
     * 根据已有用户信息登录
     *
     * @param sysUser 用户信息
     * @return token
     * @author xuyuxiang
     * @date 2020/7/29 10:12
     **/
    String doLogin(SysUser sysUser);

    /**
     * 从request获取token
     *
     * @param request request
     * @return token
     * @author xuyuxiang
     * @date 2020/3/13 11:41
     */
    String getTokenFromRequest(HttpServletRequest request);

    /**
     * 根据token获取登录用户信息
     *
     * @param token token
     * @return 当前登陆的用户信息
     * @author xuyuxiang
     * @date 2020/3/13 11:59
     */
    SysLoginUser getLoginUserByToken(String token);

    /**
     * 退出登录
     *
     * @author xuyuxiang
     * @date 2020/3/16 15:03
     */
    void logout();

    /**
     * 设置SpringSecurityContext上下文，方便获取用户
     *
     * @param sysLoginUser 当前登录用户信息
     * @author xuyuxiang
     * @date 2020/3/19 19:59
     */
    void setSpringSecurityContextAuthentication(SysLoginUser sysLoginUser);

    /**
     * 获取SpringSecurityContext中认证信息
     *
     * @return 认证信息
     * @author xuyuxiang
     * @date 2020/3/19 20:02
     */
    Authentication getAuthentication();

    /**
     * 校验token是否正确
     *
     * @param token token
     * @author xuyuxiang
     * @date 2020/5/28 9:57
     */
    void checkToken(String token);

    /**
     * 临时缓存租户信息
     *
     * @param tenantCode 多租户编码
     * @author xuyuxiang
     * @date 2020/9/3 21:22
     */
    void cacheTenantInfo(String tenantCode);

    /**
     * 根据系统用户构造用户登陆信息
     *
     * @param sysUser 系统用户
     * @return 用户信息
     * @author xuyuxiang
     * @date 2020/9/20 15:21
     **/
    SysLoginUser genSysLoginUser(SysUser sysUser);
}
