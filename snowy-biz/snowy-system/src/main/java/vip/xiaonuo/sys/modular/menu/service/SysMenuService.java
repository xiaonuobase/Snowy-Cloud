package vip.xiaonuo.sys.modular.menu.service;

import vip.xiaonuo.common.pojo.node.LoginMenuTreeNode;
import vip.xiaonuo.sys.modular.menu.entity.SysMenu;
import vip.xiaonuo.sys.modular.menu.node.MenuBaseTreeNode;
import vip.xiaonuo.sys.modular.menu.param.SysMenuParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统菜单service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 16:05
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取用户权限相关信息
     *
     * @param userId 用户id
     * @return 权限集合
     * @author xuyuxiang
     * @date 2020/3/13 16:26
     */
    List<String> getLoginPermissions(Long userId);

    /**
     * 获取用户AntDesign菜单相关信息，前端使用
     *
     * @param userId  用户id
     * @param appCode 应用编码
     * @return AntDesign菜单信息结果集
     * @author yubaoshan
     * @date 2020/4/17 17:48
     */
    List<LoginMenuTreeNode> getLoginMenusAntDesign(Long userId, String appCode);

    /**
     * 获取用户菜单所属的应用编码集合
     *
     * @param userId 用户id
     * @return 用户菜单所属的应用编码集合
     * @author xuyuxiang
     * @date 2020/3/21 11:01
     */
    List<String> getUserMenuAppCodeList(Long userId);

    /**
     * 系统菜单列表（树表）
     *
     * @param sysMenuParam 查询参数
     * @return 菜单树表列表
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    List<SysMenu> list(SysMenuParam sysMenuParam);

    /**
     * 添加系统菜单
     *
     * @param sysMenuParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void add(SysMenuParam sysMenuParam);

    /**
     * 删除系统菜单
     *
     * @param sysMenuParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void delete(SysMenuParam sysMenuParam);

    /**
     * 编辑系统菜单
     *
     * @param sysMenuParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void edit(SysMenuParam sysMenuParam);

    /**
     * 查看系统菜单
     *
     * @param sysMenuParam 查看参数
     * @return 系统菜单
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    SysMenu detail(SysMenuParam sysMenuParam);

    /**
     * 获取系统菜单树，用于新增，编辑时选择上级节点
     *
     * @param sysMenuParam 查询参数
     * @return 菜单树列表
     * @author xuyuxiang
     * @date 2020/3/27 15:56
     */
    List<MenuBaseTreeNode> tree(SysMenuParam sysMenuParam);

    /**
     * 获取系统菜单树，用于给角色授权时选择
     *
     * @param sysMenuParam 查询参数
     * @return 菜单树列表
     * @author xuyuxiang
     * @date 2020/4/5 15:01
     */
    List<MenuBaseTreeNode> treeForGrant(SysMenuParam sysMenuParam);

    /**
     * 根据应用编码判断该机构下是否有状态为正常的菜单
     *
     * @param appCode 应用编码
     * @return 该应用下是否有正常菜单，true是，false否
     * @author xuyuxiang
     * @date 2020/6/28 12:14
     */
    boolean hasMenu(String appCode);
}
