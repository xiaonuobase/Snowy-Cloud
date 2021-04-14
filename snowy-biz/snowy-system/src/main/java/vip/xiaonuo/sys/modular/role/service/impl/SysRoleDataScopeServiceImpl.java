package vip.xiaonuo.sys.modular.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.sys.modular.role.entity.SysRoleDataScope;
import vip.xiaonuo.sys.modular.role.mapper.SysRoleDataScopeMapper;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import vip.xiaonuo.sys.modular.role.service.SysRoleDataScopeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色数据范围service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:55
 */
@Service
public class SysRoleDataScopeServiceImpl extends ServiceImpl<SysRoleDataScopeMapper, SysRoleDataScope> implements SysRoleDataScopeService {

    @Override
    public void grantDataScope(SysRoleParam sysRoleParam) {
        Long roleId = sysRoleParam.getId();
        LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleDataScope::getRoleId, roleId);
        //删除所拥有数据
        this.remove(queryWrapper);
        //授权数据
        sysRoleParam.getGrantOrgIdList().forEach(orgId -> {
            SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
            sysRoleDataScope.setRoleId(roleId);
            sysRoleDataScope.setOrgId(orgId);
            this.save(sysRoleDataScope);
        });
    }

    @Override
    public List<Long> getRoleDataScopeIdList(List<Long> roleIdList) {
        List<Long> resultList = CollectionUtil.newArrayList();
        if (ObjectUtil.isNotEmpty(roleIdList)) {
            LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysRoleDataScope::getRoleId, roleIdList);
            this.list(queryWrapper).forEach(sysRoleDataScope -> resultList.add(sysRoleDataScope.getOrgId()));
        }
        return resultList;
    }

    @Override
    public void deleteRoleDataScopeListByOrgIdList(List<Long> orgIdList) {
        LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleDataScope::getOrgId, orgIdList);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteRoleDataScopeListByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleDataScope::getRoleId, roleId);
        this.remove(queryWrapper);
    }
}
