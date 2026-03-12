/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.auth.core.pojo.ClientLoginUser;
import vip.xiaonuo.auth.core.pojo.SaBaseLoginUser;
import vip.xiaonuo.auth.core.pojo.SysLoginUser;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * SysLoginUserFeign
 *
 * @author dongxiayu
 * @date 2025/10/09 23:13
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysLoginUserFeign")
public interface SysLoginUserFeign {

    /**
     * 根据id获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getUserById")
    SysLoginUser getUserById(@RequestParam("id") String id);

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getClientUserById")
    ClientLoginUser getClientUserById(@RequestParam("id") String id);

    /**
     * 根据账号获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getUserByAccount")
    SysLoginUser getUserByAccount(@RequestParam("account") String account);

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getClientUserByAccount")
    ClientLoginUser getClientUserByAccount(@RequestParam("account") String account);

    /**
     * 根据手机号获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getUserByPhone")
    SysLoginUser getUserByPhone(@RequestParam("phone") String phone);

    /**
     * 根据邮箱获取B端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getUserByEmail")
    SysLoginUser getUserByEmail(@RequestParam("email") String email);

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getClientUserByPhone")
    ClientLoginUser getClientUserByPhone(@RequestParam("phone") String phone);

    /**
     * 根据邮箱获取C端用户信息，查不到则返回null
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/getClientUserByEmail")
    ClientLoginUser getClientUserByEmail(@RequestParam("email") String email);

    /**
     * 根据用户id获取用户集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    @PostMapping("/feign/sys/login/user/listUserByUserIdList")
    String listUserByUserIdList(@RequestParam("userIdList") List<String> userIdList);

    /**
     * 根据用户id获取角色集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    @PostMapping("/feign/sys/login/user/getRoleListByUserId")
    String getRoleListByUserId(@RequestParam("userId") String userId);

    /**
     * 根据角色id和用户id集合获取按钮码集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @PostMapping("/feign/sys/login/user/getButtonCodeListListByUserAndRoleIdList")
    List<String> getButtonCodeListListByUserAndRoleIdList(@RequestParam("userAndRoleIdList") List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取移动端按钮码集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @PostMapping("/feign/sys/login/user/getMobileButtonCodeListListByUserIdAndRoleIdList")
    List<String> getMobileButtonCodeListListByUserIdAndRoleIdList(@RequestParam("userAndRoleIdList") List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取权限集合
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @PostMapping("/feign/sys/login/user/getPermissionListByUserIdAndRoleIdList")
    String getPermissionListByUserIdAndRoleIdList(@RequestParam("userAndRoleIdList") List<String> userAndRoleIdList,
                                                  @RequestParam("orgId") String orgId);

    /**
     * 更新用户的登录时间和登录ip等信息
     *
     * @author xuyuxiang
     * @date 2022/4/27 22:57
     */
    @PostMapping("/feign/sys/login/user/updateUserLoginInfo")
    void updateUserLoginInfo(@RequestParam("userId") String userId,
                             @RequestParam("device") String device);

    /**
     * 使用手机号创建B端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/createUserWithPhone")
    SysLoginUser createUserWithPhone(@RequestParam("phone") String phone);

    /**
     * 使用手机号创建C端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/createClientUserWithPhone")
    ClientLoginUser createClientUserWithPhone(@RequestParam("phone") String phone);

    /**
     * 使用邮箱创建B端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/createUserWithEmail")
    SysLoginUser createUserWithEmail(@RequestParam("email") String email);

    /**
     * 使用邮箱创建C端用户
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/createClientUserWithEmail")
    ClientLoginUser createClientUserWithEmail(@RequestParam("email") String email);

    /**
     * 执行注册
     *
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     **/
    @PostMapping("/feign/sys/login/user/doRegister")
    void doRegister(@RequestParam("account") String account,
                    @RequestParam("password") String password);

    /**
     * 刷新用户数据范围预计算表
     *
     * @author yubaoshan
     * @date 2026/3/8 16:14
     **/
    @PostMapping("/feign/sys/login/user/refreshUserDataScope")
    void refreshUserDataScope(@RequestParam("userId") String userId,
                              @RequestParam("dataScopeList") List<SaBaseLoginUser.DataScope> dataScopeList);

    /**
     * 刷新用户数据范围预计算表
     *
     * @author yubaoshan
     * @date 2026/3/8 16:14
     **/
    @PostMapping("/feign/sys/login/user/refreshOnlineUserPermission")
    void refreshOnlineUserPermission(@RequestParam("userId") String userId);

}
