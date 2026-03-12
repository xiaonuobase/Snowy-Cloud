package vip.xiaonuo.auth.api.context;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.auth.api.SaBaseLoginUserApi;
import vip.xiaonuo.auth.core.pojo.SaBaseClientLoginUser;
import vip.xiaonuo.auth.core.pojo.SaBaseLoginUser;
import vip.xiaonuo.auth.feign.SysLoginUserFeign;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component("loginUserApi")
public class SysLoginUserApiContextBean implements SaBaseLoginUserApi {

    private final SysLoginUserFeign sysLoginUserFeign;

    /**
     * 根据id获取B端用户信息，查不到则返回null
     *
     * @param id
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser getUserById(String id) {
        return this.sysLoginUserFeign.getUserById(id);
    }

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @param id
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser getClientUserById(String id) {
        return this.sysLoginUserFeign.getClientUserById(id);
    }

    /**
     * 根据账号获取B端用户信息，查不到则返回null
     *
     * @param account
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser getUserByAccount(String account) {
        return this.sysLoginUserFeign.getUserByAccount(account);
    }

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @param account
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser getClientUserByAccount(String account) {
        return this.sysLoginUserFeign.getClientUserByPhone(account);
    }

    /**
     * 根据手机号获取B端用户信息，查不到则返回null
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser getUserByPhone(String phone) {
        return this.sysLoginUserFeign.getUserByPhone(phone);
    }

    /**
     * 根据邮箱获取B端用户信息，查不到则返回null
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser getUserByEmail(String email) {
        return this.sysLoginUserFeign.getUserByEmail(email);
    }

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser getClientUserByPhone(String phone) {
        return this.sysLoginUserFeign.getClientUserByAccount(phone);
    }

    /**
     * 根据邮箱获取C端用户信息，查不到则返回null
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser getClientUserByEmail(String email) {
        return this.sysLoginUserFeign.getClientUserByEmail(email);
    }

    /**
     * 根据用户id获取用户集合
     *
     * @param userIdList
     * @author xuyuxiang
     * @date 2022/4/27 22:53
     */
    @Override
    public List<JSONObject> listUserByUserIdList(List<String> userIdList) {
        String feignResp = this.sysLoginUserFeign.listUserByUserIdList(userIdList);
        List<JSONObject> resp = null;
        if(StrUtil.isNotBlank(feignResp)){
            resp = (List<JSONObject>) JSONUtil.toBean(feignResp,List.class);
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
    public List<JSONObject> getRoleListByUserId(String userId) {
        String feignResp = this.sysLoginUserFeign.getRoleListByUserId(userId);
        List<JSONObject> resp = null;
        if(StrUtil.isNotBlank(feignResp)){
            resp = (List<JSONObject>) JSONUtil.toBean(feignResp,List.class);
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
        return this.sysLoginUserFeign.getButtonCodeListListByUserAndRoleIdList(userAndRoleIdList);
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
        return this.sysLoginUserFeign.getMobileButtonCodeListListByUserIdAndRoleIdList(userAndRoleIdList);
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
    public List<JSONObject> getPermissionListByUserIdAndRoleIdList(List<String> userAndRoleIdList, String orgId) {
        String feignResp = this.sysLoginUserFeign.getPermissionListByUserIdAndRoleIdList(userAndRoleIdList, orgId);
        List<JSONObject> resp = null;
        if(StrUtil.isNotBlank(feignResp)){
            resp = (List<JSONObject>) JSONUtil.toBean(feignResp,List.class);
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
        this.sysLoginUserFeign.updateUserLoginInfo(userId, device);
    }

    /**
     * 使用手机号创建B端用户
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser createUserWithPhone(String phone) {
        return this.sysLoginUserFeign.createUserWithPhone(phone);
    }

    /**
     * 使用手机号创建C端用户
     *
     * @param phone
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser createClientUserWithPhone(String phone) {
        return this.sysLoginUserFeign.createClientUserWithPhone(phone);
    }

    /**
     * 使用邮箱创建B端用户
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseLoginUser createUserWithEmail(String email) {
        return this.sysLoginUserFeign.createUserWithEmail(email);
    }

    /**
     * 使用邮箱创建C端用户
     *
     * @param email
     * @author xuyuxiang
     * @date 2022/3/10 16:14
     */
    @Override
    public SaBaseClientLoginUser createClientUserWithEmail(String email) {
        return this.sysLoginUserFeign.createClientUserWithEmail(email);
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
        this.sysLoginUserFeign.doRegister(account, password);
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
        this.sysLoginUserFeign.refreshUserDataScope(userId, dataScopeList);
    }

    /**
     * 刷新在线用户的权限缓存（Session），权限变更后调用，确保实时生效。
     * 如果用户不在线则跳过。
     *
     * @param userId 用户ID
     * @author xuyuxiang
     * @date 2026/3/12
     */
    @Override
    public void refreshOnlineUserPermission(String userId) {
        this.sysLoginUserFeign.refreshOnlineUserPermission(userId);
    }
}
