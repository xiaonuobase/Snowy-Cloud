package vip.xiaonuo.api.context.service;

import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * LoginContext上下文服务接口
 *
 * @author dongxiayu
 * @date 2021/3/28 14:57
 */
@RequestMapping("/feign/loginContextServiceApi")
public interface LoginContextServiceApi {

    String APP_NAME = FeignConstant.MAIN_APP;

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUser")
    SysLoginUser getSysLoginUser();

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserWithoutException")
    SysLoginUser getSysLoginUserWithoutException();

    /**
     * 获取当前登录用户的id
     *
     * @return 当前登录用户的id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserId")
    Long getSysLoginUserId();

    /**
     * 判断用户是否登录
     *
     * @return 是否登录，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasLogin")
    boolean hasLogin();

    /**
     * 获取当前登录用户的账户
     *
     * @return 当前登陆用户的账户account
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserAccount")
    String getSysLoginUserAccount();

    /**
     * 判断当前登录用户是否有某资源的访问权限
     *
     * @param requestUri 请求的url
     * @return 是否有访问权限，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasPermission")
    boolean hasPermission(@RequestParam String requestUri);

    /**
     * 判断当前登录用户是否包含某个角色
     *
     * @param roleCode 角色编码
     * @return 是否包含该角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasRole")
    boolean hasRole(@RequestParam String roleCode);

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含任一角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasAnyRole")
    boolean hasAnyRole(@RequestParam String roleCodes);

    /**
     * 判断当前登录用户是否是超级管理员
     *
     * @return 当前登录用户是否是超级管理员
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/isSuperAdmin")
    boolean isSuperAdmin();

    /**
     * 判断当前登录用户是否包含所有角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含所有角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasAllRole")
    boolean hasAllRole(@RequestParam String roleCodes);

    /**
     * 获取当前登录用户的数据范围集合（组织机构id集合）
     *
     * @return 数据范围集合（组织机构id集合）
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getLoginUserDataScopeIdList")
    List<Long> getLoginUserDataScopeIdList();

    /**
     * 获取当前登录用户的组织机构id
     *
     * @return 当前登录用户的组织机构id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserOrgId")
    Long getSysLoginUserOrgId();

    /**
     * 获取当前登录用户的角色id集合
     *
     * @return 当前登录用户角色id集合
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getLoginUserRoleIds")
    List<String> getLoginUserRoleIds();

    /**
     * 获取最新的用户信息，用于修改之后前端获取
     *
     * @return 最新的用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     **/
    @RequestMapping("/getSysLoginUserUpToDate")
    SysLoginUser getSysLoginUserUpToDate();
}
