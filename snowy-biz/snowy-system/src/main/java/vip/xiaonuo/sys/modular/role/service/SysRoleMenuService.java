package vip.xiaonuo.sys.modular.role.service;

import vip.xiaonuo.sys.modular.role.entity.SysRoleMenu;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色菜单service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:50
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 获取角色的菜单id集合
     *
     * @param roleIdList 角色id集合
     * @return 菜单id集合
     * @author xuyuxiang
     * @date 2020/3/21 10:17
     */
    List<Long> getRoleMenuIdList(List<Long> roleIdList);

    /**
     * 授权菜单
     *
     * @param sysRoleParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:36
     */
    void grantMenu(SysRoleParam sysRoleParam);

    /**
     * 根据菜单id集合删除对应的角色-菜单表信息
     *
     * @param menuIdList 菜单id集合
     * @author xuyuxiang
     * @date 2020/6/28 14:19
     */
    void deleteRoleMenuListByMenuIdList(List<Long> menuIdList);

    /**
     * 根据角色id删除对应的角色-菜单表关联信息
     *
     * @param roleId 角色id
     * @author xuyuxiang
     * @date 2020/6/28 14:43
     */
    void deleteRoleMenuListByRoleId(Long roleId);
}
