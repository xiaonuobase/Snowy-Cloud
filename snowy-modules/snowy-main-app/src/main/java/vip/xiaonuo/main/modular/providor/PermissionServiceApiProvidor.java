package vip.xiaonuo.main.modular.providor;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import vip.xiaonuo.api.auth.service.PermissionServiceApi;
import vip.xiaonuo.cache.AllPermissionCache;
import vip.xiaonuo.common.enums.CommonStatusEnum;
import vip.xiaonuo.sys.core.enums.MenuTypeEnum;
import vip.xiaonuo.sys.modular.menu.entity.SysMenu;
import vip.xiaonuo.sys.modular.menu.service.SysMenuService;
import vip.xiaonuo.sys.modular.role.entity.SysRoleMenu;
import vip.xiaonuo.sys.modular.role.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : dongxiayu
 * @classname : PermissionServiceApiProvidor
 * @description : 权限业务服务接口
 * @date : 2021/4/8 16:21
 */
@RestController
public class PermissionServiceApiProvidor implements PermissionServiceApi {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private AllPermissionCache allPermissionCache;

    /**
     * 获取所有权限信息
     *
     * @return 所有权限信息
     * @author dongxiayu
     * @date 2021/4/8 17:51
     */
    @Override
    public List<String> getAllPermission() {

        List<String> allPermissionList = allPermissionCache.get(AllPermissionCache.ALL_PERMISSION_CACHE_PREFIX);
        if(CollectionUtil.isNotEmpty(allPermissionList)){
            return allPermissionList;
        }

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list();
        if (CollectionUtil.isNotEmpty(sysRoleMenuList)) {
            List<Long> menuIdList = new ArrayList<>();
            sysRoleMenuList.forEach(sysRoleMenu -> menuIdList.add(sysRoleMenu.getMenuId()));

            LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysMenu::getId, menuIdList).eq(SysMenu::getType, MenuTypeEnum.BTN.getCode())
                    .eq(SysMenu::getStatus, CommonStatusEnum.ENABLE.getCode());

            List<String> finalAllPermissionList = new ArrayList<>();
            sysMenuService.list(queryWrapper).forEach(sysMenu -> finalAllPermissionList.add(sysMenu.getPermission()));
            allPermissionList = finalAllPermissionList;
            allPermissionCache.put(AllPermissionCache.ALL_PERMISSION_CACHE_PREFIX,allPermissionList);
        }
        return allPermissionList;
    }
}
