package vip.xiaonuo.sys.modular.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import vip.xiaonuo.sys.modular.user.entity.SysUserDataScope;
import vip.xiaonuo.sys.modular.user.mapper.SysUserDataScopeMapper;
import vip.xiaonuo.api.auth.param.SysUserParam;
import vip.xiaonuo.sys.modular.user.service.SysUserDataScopeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户数据范围service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:49
 */
@Service
public class SysUserDataScopeServiceImpl extends ServiceImpl<SysUserDataScopeMapper, SysUserDataScope>
        implements SysUserDataScopeService {

    @Override
    public void grantData(SysUserParam sysUserParam) {
        Long userId = sysUserParam.getId();
        //删除所拥有数据
        LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDataScope::getUserId, userId);
        this.remove(queryWrapper);
        //授权数据
        sysUserParam.getGrantOrgIdList().forEach(orgId -> {
            SysUserDataScope sysUserDataScope = new SysUserDataScope();
            sysUserDataScope.setUserId(userId);
            sysUserDataScope.setOrgId(orgId);
            this.save(sysUserDataScope);
        });
    }

    @Override
    public List<Long> getUserDataScopeIdList(Long uerId) {
        List<Long> userDataScopeIdList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDataScope::getUserId, uerId);
        this.list(queryWrapper).forEach(sysUserDataScope -> userDataScopeIdList.add(sysUserDataScope.getOrgId()));
        return userDataScopeIdList;
    }

    @Override
    public void deleteUserDataScopeListByOrgIdList(List<Long> orgIdList) {
        LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserDataScope::getOrgId, orgIdList);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteUserDataScopeListByUserId(Long userId) {
        LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDataScope::getUserId, userId);
        this.remove(queryWrapper);
    }
}
