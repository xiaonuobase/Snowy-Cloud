package vip.xiaonuo.sys.modular.role.service;

import vip.xiaonuo.sys.modular.role.entity.SysRoleDataScope;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色数据范围service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:51
 */
public interface SysRoleDataScopeService extends IService<SysRoleDataScope> {

    /**
     * 授权数据
     *
     * @param sysRoleParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:36
     */
    void grantDataScope(SysRoleParam sysRoleParam);

    /**
     * 根据角色id获取角色数据范围集合
     *
     * @param roleIdList 角色id集合
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/4/5 17:44
     */
    List<Long> getRoleDataScopeIdList(List<Long> roleIdList);

    /**
     * 根据机构id集合删除对应的角色-数据范围关联信息
     *
     * @param orgIdList 机构id集合
     * @author xuyuxiang
     * @date 2020/6/28 14:14
     */
    void deleteRoleDataScopeListByOrgIdList(List<Long> orgIdList);

    /**
     * 根据角色id删除对应的角色-数据范围关联信息
     *
     * @param roleId 角色id
     * @author xuyuxiang
     * @date 2020/6/28 14:47
     */
    void deleteRoleDataScopeListByRoleId(Long roleId);
}
