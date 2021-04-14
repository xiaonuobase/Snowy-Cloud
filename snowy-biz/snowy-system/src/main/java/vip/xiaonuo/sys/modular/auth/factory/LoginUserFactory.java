package vip.xiaonuo.sys.modular.auth.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.common.exception.ServiceException;
import vip.xiaonuo.common.exception.enums.ServerExceptionEnum;
import vip.xiaonuo.common.pojo.login.LoginEmpInfo;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.core.tenant.consts.TenantConstants;
import vip.xiaonuo.core.tenant.context.TenantCodeHolder;
import vip.xiaonuo.core.tenant.context.TenantDbNameHolder;
import vip.xiaonuo.core.util.HttpServletUtil;
import vip.xiaonuo.core.util.IpAddressUtil;
import vip.xiaonuo.core.util.UaUtil;
import vip.xiaonuo.sys.modular.app.service.SysAppService;
import vip.xiaonuo.sys.modular.emp.service.SysEmpService;
import vip.xiaonuo.sys.modular.menu.service.SysMenuService;
import vip.xiaonuo.sys.modular.role.service.SysRoleService;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登录用户工厂类
 *
 * @author xuyuxiang
 * @date 2020/3/13 14:58
 */
public class LoginUserFactory {

    private static final SysUserService sysUserService = SpringUtil.getBean(SysUserService.class);

    private static final SysEmpService sysEmpService = SpringUtil.getBean(SysEmpService.class);

    private static final SysAppService sysAppService = SpringUtil.getBean(SysAppService.class);

    private static final SysMenuService sysMenuService = SpringUtil.getBean(SysMenuService.class);

    private static final SysRoleService sysRoleService = SpringUtil.getBean(SysRoleService.class);

    /**
     * 填充登录用户相关信息
     *
     * @author xuyuxiang yubaoshan
     * @date 2020/3/13 15:01
     */
    public static void fillLoginUserInfo(SysLoginUser sysLoginUser) {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotNull(request)) {
            sysLoginUser.setLastLoginIp(IpAddressUtil.getIp(request));
            sysLoginUser.setLastLoginTime(DateTime.now().toString());
            sysLoginUser.setLastLoginAddress(IpAddressUtil.getAddress(request));
            sysLoginUser.setLastLoginBrowser(UaUtil.getBrowser(request));
            sysLoginUser.setLastLoginOs(UaUtil.getOs(request));
            Long userId = sysLoginUser.getId();

            // 员工信息
            LoginEmpInfo loginEmpInfo = sysEmpService.getLoginEmpInfo(userId);
            sysLoginUser.setLoginEmpInfo(loginEmpInfo);

            // 角色信息
            List<Dict> roles = sysRoleService.getLoginRoles(userId);
            sysLoginUser.setRoles(roles);

            // 权限信息
            List<String> permissions = sysMenuService.getLoginPermissions(userId);
            sysLoginUser.setPermissions(permissions);

            // 数据范围信息
            List<Long> dataScopes = sysUserService.getUserDataScopeIdList(userId, loginEmpInfo.getOrgId());
            sysLoginUser.setDataScopes(dataScopes);

            // 具备应用信息（多系统，默认激活一个，可根据系统切换菜单）,返回的结果中第一个为激活的系统
            List<Dict> apps = sysAppService.getLoginApps(userId);
            sysLoginUser.setApps(apps);

            // 如果根本没有应用信息，则没有菜单信息
            if (ObjectUtil.isEmpty(apps)) {
                sysLoginUser.setMenus(CollectionUtil.newArrayList());
            } else {
                //AntDesign菜单信息，根据人获取，用于登录后展示菜单树，默认获取默认激活的系统的菜单
                String defaultActiveAppCode = apps.get(0).getStr(CommonConstant.CODE);
                sysLoginUser.setMenus(sysMenuService.getLoginMenusAntDesign(userId, defaultActiveAppCode));
            }

            //如果开启了多租户功能，则设置当前登录用户的租户标识
            if (ConstantContextHolder.getTenantOpenFlag()) {
                String tenantCode = TenantCodeHolder.get();
                String dataBaseName = TenantDbNameHolder.get();
                if (StrUtil.isNotBlank(tenantCode) && StrUtil.isNotBlank(dataBaseName)) {
                    Dict tenantInfo = Dict.create();
                    tenantInfo.set(TenantConstants.TENANT_CODE, tenantCode);
                    tenantInfo.set(TenantConstants.TENANT_DB_NAME, dataBaseName);
                    sysLoginUser.setTenants(tenantInfo);
                }
                //注意，这里remove不代表所有情况，在aop remove
                TenantCodeHolder.remove();
                TenantDbNameHolder.remove();
            }

        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }
}
