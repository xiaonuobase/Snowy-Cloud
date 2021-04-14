package vip.xiaonuo.main.modular.providor;

import vip.xiaonuo.api.context.service.LoginContextServiceApi;
import vip.xiaonuo.common.context.login.LoginContextHolder;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : dongxiayu
 * @classname : LoginContextServiceController
 * @description : LoginContext上下文服务接口
 * @date : 2021/3/28 17:53
 */
@RestController
public class LoginContextServiceApiProvidor implements LoginContextServiceApi {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public SysLoginUser getSysLoginUser() {
        return LoginContextHolder.me().getSysLoginUser();
    }

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public SysLoginUser getSysLoginUserWithoutException() {
        return LoginContextHolder.me().getSysLoginUserWithoutException();
    }

    /**
     * 获取当前登录用户的id
     *
     * @return 当前登录用户的id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public Long getSysLoginUserId() {
        return LoginContextHolder.me().getSysLoginUserId();
    }

    /**
     * 判断用户是否登录
     *
     * @return 是否登录，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean hasLogin() {
        return LoginContextHolder.me().hasLogin();
    }

    /**
     * 获取当前登录用户的账户
     *
     * @return 当前登陆用户的账户account
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public String getSysLoginUserAccount() {
        return LoginContextHolder.me().getSysLoginUserAccount();
    }

    /**
     * 判断当前登录用户是否有某资源的访问权限
     *
     * @param requestUri 请求的url
     * @return 是否有访问权限，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean hasPermission(String requestUri) {
        return LoginContextHolder.me().hasPermission(requestUri);
    }

    /**
     * 判断当前登录用户是否包含某个角色
     *
     * @param roleCode 角色编码
     * @return 是否包含该角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean hasRole(String roleCode) {
        return LoginContextHolder.me().hasRole(roleCode);
    }

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含任一角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean hasAnyRole(String roleCodes) {
        return LoginContextHolder.me().hasAnyRole(roleCodes);
    }

    /**
     * 判断当前登录用户是否是超级管理员
     *
     * @return 当前登录用户是否是超级管理员
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean isSuperAdmin() {
        return LoginContextHolder.me().isSuperAdmin();
    }

    /**
     * 判断当前登录用户是否包含所有角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含所有角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean hasAllRole(String roleCodes) {
        return LoginContextHolder.me().hasAllRole(roleCodes);
    }

    /**
     * 获取当前登录用户的数据范围集合（组织机构id集合）
     *
     * @return 数据范围集合（组织机构id集合）
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public List<Long> getLoginUserDataScopeIdList() {
        return LoginContextHolder.me().getLoginUserDataScopeIdList();
    }

    /**
     * 获取当前登录用户的组织机构id
     *
     * @return 当前登录用户的组织机构id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public Long getSysLoginUserOrgId() {
        return LoginContextHolder.me().getSysLoginUserOrgId();
    }

    /**
     * 获取当前登录用户的角色id集合
     *
     * @return 当前登录用户角色id集合
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public List<String> getLoginUserRoleIds() {
        return LoginContextHolder.me().getLoginUserRoleIds();
    }

    /**
     * 获取最新的用户信息，用于修改之后前端获取
     *
     * @return 最新的用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     **/
    @Override
    public SysLoginUser getSysLoginUserUpToDate() {
        return LoginContextHolder.me().getSysLoginUserUpToDate();
    }
}
