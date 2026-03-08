package vip.xiaonuo.auth.feign.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.auth.api.SaBaseLoginUserApi;
import vip.xiaonuo.auth.core.pojo.ClientLoginUser;
import vip.xiaonuo.auth.core.pojo.SaBaseClientLoginUser;
import vip.xiaonuo.auth.core.pojo.SaBaseLoginUser;
import vip.xiaonuo.auth.core.pojo.SysLoginUser;
import vip.xiaonuo.auth.feign.SysLoginUserFeign;

import java.util.List;
import java.util.Objects;

/**
 * SysLoginUserFeignProvider
 *
 * @author dongxiayu
 * @date 2025/10/09 23:23
 */
@Slf4j
@RestController
public class SysLoginUserFeignProvider implements SysLoginUserFeign {

    /**
     * loginUserApi
     */
    @Resource(name = "loginUserApi")
    private SaBaseLoginUserApi loginUserApi;

    /**
     * 根据id获取B端用户信息，查不到则返回null
     *
     * @param id
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser getUserById(String id) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.getUserById(id);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @param id
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser getClientUserById(String id) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.getClientUserById(id);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 根据账号获取B端用户信息，查不到则返回null
     *
     * @param account
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser getUserByAccount(String account) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.getUserByAccount(account);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @param account
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser getClientUserByAccount(String account) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.getClientUserByAccount(account);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 根据手机号获取B端用户信息，查不到则返回null
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser getUserByPhone(String phone) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.getUserByPhone(phone);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 根据邮箱获取B端用户信息，查不到则返回null
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser getUserByEmail(String email) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.getUserByEmail(email);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser getClientUserByPhone(String phone) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.getClientUserByPhone(phone);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 根据邮箱获取C端用户信息，查不到则返回null
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser getClientUserByEmail(String email) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.getClientUserByEmail(email);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 根据用户id获取用户集合
     *
     * @param userIdList
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    @Override
    public String listUserByUserIdList(List<String> userIdList) {
        String resp = null;

        List<JSONObject> jsonObjectList = this.loginUserApi.listUserByUserIdList(userIdList);

        if(CollUtil.isNotEmpty(jsonObjectList)){
            resp = JSONUtil.toJsonStr(jsonObjectList);
        }

        return resp;
    }

    /**
     * 根据用户id获取角色集合
     *
     * @param userId
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    @Override
    public String getRoleListByUserId(String userId) {
        String resp = null;

        List<JSONObject> jsonObjectList = this.loginUserApi.getRoleListByUserId(userId);

        if(CollUtil.isNotEmpty(jsonObjectList)){
            resp = JSONUtil.toJsonStr(jsonObjectList);
        }

        return resp;
    }

    /**
     * 根据角色id和用户id集合获取按钮码集合
     *
     * @param userAndRoleIdList
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @Override
    public List<String> getButtonCodeListListByUserAndRoleIdList(List<String> userAndRoleIdList) {
        return this.loginUserApi.getButtonCodeListListByUserAndRoleIdList(userAndRoleIdList);
    }

    /**
     * 根据角色id和用户id集合获取移动端按钮码集合
     *
     * @param userAndRoleIdList
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @Override
    public List<String> getMobileButtonCodeListListByUserIdAndRoleIdList(List<String> userAndRoleIdList) {
        return this.loginUserApi.getMobileButtonCodeListListByUserIdAndRoleIdList(userAndRoleIdList);
    }

    /**
     * 根据角色id和用户id集合获取权限集合
     *
     * @param userAndRoleIdList
     * @param orgId
     * @author xuyuxiang
     * @date 2022/4/27 22:54
     */
    @Override
    public String getPermissionListByUserIdAndRoleIdList(List<String> userAndRoleIdList, String orgId) {
        String resp = null;

        List<JSONObject> jsonObjectList = this.loginUserApi.getPermissionListByUserIdAndRoleIdList(userAndRoleIdList, orgId);

        if(CollUtil.isNotEmpty(jsonObjectList)){
            resp = JSONUtil.toJsonStr(jsonObjectList);
        }

        return resp;
    }

    /**
     * 更新用户的登录时间和登录ip等信息
     *
     * @param userId
     * @param device
     * @author xuyuxiang
     * @date 2022/4/27 22:57
     */
    @Override
    public void updateUserLoginInfo(String userId, String device) {
        this.loginUserApi.updateUserLoginInfo(userId, device);
    }

    /**
     * 使用手机号创建B端用户
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser createUserWithPhone(String phone) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.createUserWithPhone(phone);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 使用手机号创建C端用户
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser createClientUserWithPhone(String phone) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.createClientUserWithPhone(phone);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 使用邮箱创建B端用户
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SysLoginUser createUserWithEmail(String email) {
        SysLoginUser sysLoginUser = null;

        SaBaseLoginUser baseLoginUser = this.loginUserApi.createUserWithEmail(email);
        if(Objects.nonNull(baseLoginUser)){
            sysLoginUser = (SysLoginUser) baseLoginUser;
        }

        return sysLoginUser;
    }

    /**
     * 使用邮箱创建C端用户
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public ClientLoginUser createClientUserWithEmail(String email) {
        ClientLoginUser clientLoginUser = null;

        SaBaseClientLoginUser baseClientLoginUser = this.loginUserApi.createClientUserWithEmail(email);
        if(Objects.nonNull(baseClientLoginUser)){
            clientLoginUser = (ClientLoginUser) baseClientLoginUser;
        }

        return clientLoginUser;
    }

    /**
     * 执行注册
     *
     * @param account
     * @param password
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public void doRegister(String account, String password) {
        this.loginUserApi.doRegister(account, password);
    }

    /**
     * 刷新用户数据范围预计算表
     *
     * @param userId 用户ID
     * @param dataScopeList 用户的数据范围集合（per-API维度）
     * @author yubaoshan
     * @date 2026/2/12
     */
    @Override
    public void refreshUserDataScope(String userId, List<SaBaseLoginUser.DataScope> dataScopeList) {
        this.loginUserApi.refreshUserDataScope(userId, dataScopeList);
    }
}
