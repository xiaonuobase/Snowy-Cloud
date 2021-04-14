package vip.xiaonuo.sys.modular.auth.context;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.api.auth.service.AuthService;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.consts.SymbolConstant;
import vip.xiaonuo.common.context.login.LoginContext;
import vip.xiaonuo.common.exception.AuthException;
import vip.xiaonuo.common.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.common.pojo.login.LoginEmpInfo;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.sys.core.enums.AdminTypeEnum;
import vip.xiaonuo.sys.modular.user.service.SysUserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录用户上下文实现类
 * @author xuyuxiang
 * @date 2020/3/13 12:19
 */
@Component
public class LoginContextSpringSecurityImpl implements LoginContext {

    @Resource
    private AuthService authService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 获取当前登录用户
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:42
     */
    @Override
    public SysLoginUser getSysLoginUser() {
        Authentication authentication = authService.getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            throw new AuthException(AuthExceptionEnum.NO_LOGIN_USER);
        } else {
            return (SysLoginUser) authentication.getPrincipal();
        }
    }

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:42
     */
    @Override
    public SysLoginUser getSysLoginUserWithoutException() {
        Authentication authentication = authService.getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            return null;
        } else {
            return (SysLoginUser) authentication.getPrincipal();
        }
    }

    /**
     * 获取当前登录用户的id
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:26
     */
    @Override
    public Long getSysLoginUserId() {
        return this.getSysLoginUser().getId();
    }

    /**
     * 判断用户是否登录
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:23
     */
    @Override
    public boolean hasLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            return false;
        } else {
            return !(authentication instanceof AnonymousAuthenticationToken);
        }
    }

    /**
     * 获取当前登录的用户账号
     *
     * @author xuyuxiang
     * @date 2020/3/23 8:49
     */
    @Override
    public String getSysLoginUserAccount() {
        return this.getSysLoginUser().getAccount();
    }

    /**
     * 判断当前登录用户是否有某资源的访问权限
     *
     * @author xuyuxiang
     * @date 2020/3/23 8:49
     */
    @Override
    public boolean hasPermission(String requestUri) {
        String removePrefix = StrUtil.removePrefix(requestUri, SymbolConstant.LEFT_DIVIDE);
        String requestPermission = removePrefix.replaceAll(SymbolConstant.LEFT_DIVIDE, SymbolConstant.COLON);
        return this.getSysLoginUser().getPermissions().contains(requestPermission);
    }

    /**
     * 判断当前登录用户是否包含某个角色
     *
     * @author xuyuxiang
     * @date 2020/3/23 8:55
     */
    @Override
    public boolean hasRole(String roleCode) {
        List<String> roleCodeList = this.getLoginUserRoleCodeList();
        return roleCodeList.contains(roleCode);
    }

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     * @author xuyuxiang
     * @date 2020/3/23 8:55
     */
    @Override
    public boolean hasAnyRole(String roleCodes) {
        boolean flag = false;
        List<String> loginUserRoleCodeList = this.getLoginUserRoleCodeList();
        String[] roleCodeArr = StrUtil.split(roleCodes, SymbolConstant.COMMA);
        for (String roleCode : roleCodeArr) {
            if (loginUserRoleCodeList.contains(roleCode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 管理员类型（0超级管理员 1非管理员）
     * 判断当前登录用户是否是超级管理员
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:51
     */
    @Override
    public boolean isSuperAdmin() {
        return this.isAdmin(AdminTypeEnum.SUPER_ADMIN.getCode());
    }

    /**
     * 判断当前登录用户是否包含所有角色
     *
     * @author xuyuxiang
     * @date 2020/4/5 10:28
     */
    @Override
    public boolean hasAllRole(String roleCodes) {
        boolean flag = true;
        List<String> loginUserRoleCodeList = this.getLoginUserRoleCodeList();
        String[] roleCodeArr = StrUtil.split(roleCodes, SymbolConstant.COMMA);
        for (String roleCode : roleCodeArr) {
            if (!loginUserRoleCodeList.contains(roleCode)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断当前登录用户是否是指定类型的管理员
     *
     * @author xuyuxiang
     * @date 2020/4/5 11:43
     */
    private boolean isAdmin(Integer adminTypeCode) {
        Integer adminType = this.getSysLoginUser().getAdminType();
        boolean flag = false;
        if (adminType.equals(adminTypeCode)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 当前登录用户的数据范围（组织机构id集合）
     *
     * @author xuyuxiang
     * @date 2020/4/5 17:20
     */
    @Override
    public List<Long> getLoginUserDataScopeIdList() {
        return this.getSysLoginUser().getDataScopes();
    }

    /**
     * 获取当前登录用户的组织机构id集合
     *
     * @author xuyuxiang
     * @date 2020/4/5 18:32
     */
    @Override
    public Long getSysLoginUserOrgId() {
        LoginEmpInfo loginEmpInfo = this.getSysLoginUser().getLoginEmpInfo();
        if (ObjectUtil.isNotNull(loginEmpInfo)) {
            if (ObjectUtil.isNotEmpty(loginEmpInfo.getOrgId())) {
                return loginEmpInfo.getOrgId();
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户的角色id集合
     *
     * @author xuyuxiang
     * @date 2020/4/20 16:04
     */
    @Override
    public List<String> getLoginUserRoleIds() {
        List<String> resultList = CollectionUtil.newArrayList();
        this.getSysLoginUser().getRoles().forEach(dict -> resultList.add(dict.getStr(CommonConstant.ID)));
        return resultList;
    }

    @Override
    public SysLoginUser getSysLoginUserUpToDate() {
        SysLoginUser sysLoginUser = this.getSysLoginUser();
        Long loginUserId = sysLoginUser.getId();
        SysUser sysUser = sysUserService.getById(loginUserId);
        //构造SysLoginUser
        return authService.genSysLoginUser(sysUser);
    }

    /**
     * 获取当前用户的角色编码集合
     *
     * @author xuyuxiang
     * @date 2020/3/23 8:58
     */
    private List<String> getLoginUserRoleCodeList() {
        List<String> roleCodeList = CollectionUtil.newArrayList();
        this.getSysLoginUser().getRoles().forEach(dict -> roleCodeList.add(dict.getStr(CommonConstant.CODE)));
        return roleCodeList;
    }
}
