package vip.xiaonuo.sys.modular.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import vip.xiaonuo.sys.modular.role.entity.SysRoleMenu;
import vip.xiaonuo.sys.modular.role.mapper.SysRoleMenuMapper;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import vip.xiaonuo.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色菜单service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:55
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    public List<Long> getRoleMenuIdList(List<Long> roleIdList) {
        List<Long> menuIdList = CollectionUtil.newArrayList();

        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(SysRoleMenu::getRoleId, roleIdList);

        this.list(queryWrapper).forEach(sysRoleMenu -> menuIdList.add(sysRoleMenu.getMenuId()));

        return menuIdList;
    }

    @Override
    public void grantMenu(SysRoleParam sysRoleParam) {
        Long roleId = sysRoleParam.getId();
        //删除所拥有菜单
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(queryWrapper);
        //授权菜单
        sysRoleParam.getGrantMenuIdList().forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            this.save(sysRoleMenu);
        });
    }

    @Override
    public void deleteRoleMenuListByMenuIdList(List<Long> menuIdList) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getMenuId, menuIdList);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteRoleMenuListByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(queryWrapper);
    }
}
