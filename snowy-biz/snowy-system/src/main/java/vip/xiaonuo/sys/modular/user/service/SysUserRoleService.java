package vip.xiaonuo.sys.modular.user.service;

import vip.xiaonuo.sys.modular.user.entity.SysUserRole;
import vip.xiaonuo.api.auth.param.SysUserParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统用户角色service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:44
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 获取用户的角色id集合
     *
     * @param userId 用户id
     * @return 角色id集合
     * @author xuyuxiang
     * @date 2020/3/20 22:29
     */
    List<Long> getUserRoleIdList(Long userId);

    /**
     * 授权角色
     *
     * @param sysUserParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:57
     */
    void grantRole(SysUserParam sysUserParam);

    /**
     * 获取用户所有角色的数据范围（组织机构id集合）
     *
     * @param userId 用户id
     * @param orgId  组织机构id
     * @return 数据范围id集合（组织机构id集合）
     * @author xuyuxiang
     * @date 2020/4/5 17:31
     */
    List<Long> getUserRoleDataScopeIdList(Long userId, Long orgId);

    /**
     * 根据角色id删除对应的用户-角色表关联信息
     *
     * @param roleId 角色id
     * @author xuyuxiang
     * @date 2020/6/28 14:22
     */
    void deleteUserRoleListByRoleId(Long roleId);

    /**
     * 根据用户id删除对应的用户-角色表关联信息
     *
     * @param userId 用户id
     * @author xuyuxiang
     * @date 2020/6/28 14:52
     */
    void deleteUserRoleListByUserId(Long userId);
}
