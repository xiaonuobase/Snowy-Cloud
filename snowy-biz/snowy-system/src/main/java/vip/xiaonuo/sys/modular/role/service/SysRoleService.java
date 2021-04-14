package vip.xiaonuo.sys.modular.role.service;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.role.entity.SysRole;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:47
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取用户角色相关信息
     *
     * @param userId 用户id
     * @return 增强版hashMap，格式：[{"id":456, "code":"zjl", "name":"总经理"}]
     * @author xuyuxiang
     * @date 2020/3/13 16:26
     */
    List<Dict> getLoginRoles(Long userId);

    /**
     * 查询系统角色
     *
     * @param sysRoleParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/28 14:53
     */
    PageResult<SysRole> page(SysRoleParam sysRoleParam);

    /**
     * 根据角色名模糊搜索系统角色列表
     *
     * @param sysRoleParam 查询参数
     * @return 增强版hashMap，格式：[{"id":456, "name":"总经理(zjl)"}]
     * @author xuyuxiang
     * @date 2020/4/14 9:21
     */
    List<Dict> list(SysRoleParam sysRoleParam);

    /**
     * 系统角色下拉（用于授权角色时选择）
     *
     * @return 增强版hashMap，格式：[{"id":456, "code":"zjl", "name":"总经理"}]
     * @author xuyuxiang
     * @date 2020/4/5 16:47
     */
    List<Dict> dropDown();

    /**
     * 添加系统角色
     *
     * @param sysRoleParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/28 14:54
     */
    void add(SysRoleParam sysRoleParam);

    /**
     * 删除系统角色
     *
     * @param sysRoleParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/28 14:54
     */
    void delete(SysRoleParam sysRoleParam);

    /**
     * 编辑系统角色
     *
     * @param sysRoleParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/28 14:54
     */
    void edit(SysRoleParam sysRoleParam);

    /**
     * 查看系统角色
     *
     * @param sysRoleParam 查看参数
     * @return 系统角色
     * @author xuyuxiang
     * @date 2020/3/26 9:50
     */
    SysRole detail(SysRoleParam sysRoleParam);

    /**
     * 授权菜单
     *
     * @param sysRoleParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:19
     */
    void grantMenu(SysRoleParam sysRoleParam);

    /**
     * 授权数据
     *
     * @param sysRoleParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:20
     */
    void grantData(SysRoleParam sysRoleParam);

    /**
     * 根据角色id集合获取数据范围id集合
     *
     * @param roleIdList 角色id集合
     * @param orgId      机构id
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/4/5 17:41
     */
    List<Long> getUserDataScopeIdList(List<Long> roleIdList, Long orgId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     * @return 角色名称
     * @author xuyuxiang
     * @date 2020/5/22 16:15
     */
    String getNameByRoleId(Long roleId);

    /**
     * 查询角色拥有菜单
     *
     * @param sysRoleParam 查询参数
     * @return 菜单id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:03
     */
    List<Long> ownMenu(SysRoleParam sysRoleParam);

    /**
     * 查询角色拥有数据
     *
     * @param sysRoleParam 查询参数
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:04
     */
    List<Long> ownData(SysRoleParam sysRoleParam);
}
