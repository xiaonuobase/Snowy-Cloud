package vip.xiaonuo.sys.modular.emp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.common.pojo.login.LoginEmpInfo;
import vip.xiaonuo.sys.modular.emp.entity.SysEmp;
import vip.xiaonuo.sys.modular.emp.mapper.SysEmpMapper;
import vip.xiaonuo.api.auth.param.SysEmpParam;
import vip.xiaonuo.api.auth.result.SysEmpInfo;
import vip.xiaonuo.sys.modular.emp.service.SysEmpExtOrgPosService;
import vip.xiaonuo.sys.modular.emp.service.SysEmpPosService;
import vip.xiaonuo.sys.modular.emp.service.SysEmpService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:10
 */
@Service
public class SysEmpServiceImpl extends ServiceImpl<SysEmpMapper, SysEmp> implements SysEmpService {

    @Resource
    private SysEmpExtOrgPosService sysEmpExtOrgPosService;

    @Resource
    private SysEmpPosService sysEmpPosService;

    @Override
    public LoginEmpInfo getLoginEmpInfo(Long empId) {

        LoginEmpInfo loginEmpInfo = new LoginEmpInfo();
        //获取员工信息
        SysEmp sysEmp = this.getById(empId);
        if (ObjectUtil.isNotNull(sysEmp)) {
            BeanUtil.copyProperties(sysEmp, loginEmpInfo);
            //获取附属机构和职位信息
            List<Dict> empExtOrgPosDictList = sysEmpExtOrgPosService.getEmpExtOrgPosDictList(sysEmp.getId(), false);
            loginEmpInfo.setExtOrgPos(empExtOrgPosDictList);

            //获取所属职位信息
            List<Dict> empEmpPosDictList = sysEmpPosService.getEmpPosDictList(sysEmp.getId(), false);
            loginEmpInfo.setPositions(empEmpPosDictList);
        } else {
            loginEmpInfo.setExtOrgPos(CollectionUtil.newArrayList());
            loginEmpInfo.setPositions(CollectionUtil.newArrayList());
        }
        return loginEmpInfo;
    }

    @Override
    public SysEmpInfo getSysEmpInfo(Long empId) {
        SysEmpInfo sysEmpInfo = new SysEmpInfo();
        //获取员工信息
        SysEmp sysEmp = this.getById(empId);
        if (ObjectUtil.isNotNull(sysEmp)) {
            BeanUtil.copyProperties(sysEmp, sysEmpInfo);
            //获取附属机构和职位信息
            List<Dict> empExtOrgPosDictList = sysEmpExtOrgPosService.getEmpExtOrgPosDictList(sysEmp.getId(), true);
            sysEmpInfo.setExtOrgPos(empExtOrgPosDictList);

            //获取所属职位信息
            List<Dict> empEmpPosDictList = sysEmpPosService.getEmpPosDictList(sysEmp.getId(), true);
            sysEmpInfo.setPositions(empEmpPosDictList);
        } else {
            sysEmpInfo.setExtOrgPos(CollectionUtil.newArrayList());
            sysEmpInfo.setPositions(CollectionUtil.newArrayList());
        }
        return sysEmpInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrUpdate(SysEmpParam sysEmpParam) {
        Long empId = sysEmpParam.getId();
        SysEmp sysEmp = this.getById(empId);
        if (ObjectUtil.isNull(sysEmp)) {
            sysEmp = new SysEmp();
        }
        BeanUtil.copyProperties(sysEmpParam, sysEmp);
        this.saveOrUpdate(sysEmp);
        //编辑附属机构职位相关信息
        List<Dict> extIdList = sysEmpParam.getExtIds();
        sysEmpExtOrgPosService.addOrEdit(empId, extIdList);
        //编辑职位相关信息
        List<Long> posIdList = sysEmpParam.getPosIdList();
        sysEmpPosService.addOrEdit(empId, posIdList);
    }

    @Override
    public void updateEmpOrgInfo(Long orgId, String orgName) {
        LambdaQueryWrapper<SysEmp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysEmp::getOrgId, orgId);
        this.list(queryWrapper).forEach(sysEmp -> {
            sysEmp.setOrgName(orgName);
            this.updateById(sysEmp);
        });
    }

    @Override
    public boolean hasOrgEmp(Long orgId) {
        LambdaQueryWrapper<SysEmp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysEmp::getOrgId, orgId);
        List<SysEmp> list = this.list(queryWrapper);
        return list.size() != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteEmpInfoByUserId(Long empId) {
        //删除员工信息
        this.removeById(empId);

        //级联删除对应的员工-附属信息
        sysEmpExtOrgPosService.deleteEmpExtInfoByUserId(empId);

        //级联删除对用的员工-职位信息
        sysEmpPosService.deleteEmpPosInfoByUserId(empId);
    }
}
