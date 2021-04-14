package vip.xiaonuo.sys.modular.emp.service;

import vip.xiaonuo.common.pojo.login.LoginEmpInfo;
import vip.xiaonuo.sys.modular.emp.entity.SysEmp;
import vip.xiaonuo.api.auth.param.SysEmpParam;
import vip.xiaonuo.api.auth.result.SysEmpInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 员工service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:08
 */
public interface SysEmpService extends IService<SysEmp> {

    /**
     * 获取登录用户员工相关信息
     *
     * @param empId 员工id（用户id）
     * @return 登录用户员工相关信息
     * @author xuyuxiang
     * @date 2020/3/13 15:23
     */
    LoginEmpInfo getLoginEmpInfo(Long empId);

    /**
     * 获取用户员工相关信息
     *
     * @param empId 员工id（用户id）
     * @return 用户员工相关信息
     * @author xuyuxiang
     * @date 2020/4/2 20:33
     */
    SysEmpInfo getSysEmpInfo(Long empId);

    /**
     * 增加或编辑员工相关信息
     *
     * @param sysEmpParam 增加或编辑参数
     * @author xuyuxiang
     * @date 2020/4/2 8:44
     */
    void addOrUpdate(SysEmpParam sysEmpParam);

    /**
     * 修改员工相关机构信息
     *
     * @param orgId   机构id
     * @param orgName 机构名称
     * @author xuyuxiang
     * @date 2020/6/23 9:57
     */
    void updateEmpOrgInfo(Long orgId, String orgName);

    /**
     * 根据机构id判断该机构下是否有员工
     *
     * @param orgId 机构id
     * @return 该机构下是否有员工，true是，false否
     * @author xuyuxiang
     * @date 2020/6/23 10:30
     */
    boolean hasOrgEmp(Long orgId);

    /**
     * 根据员工id删除对应的员工表信息
     *
     * @param empId 员工id（用户id）
     * @author xuyuxiang
     * @date 2020/6/28 14:51
     */
    void deleteEmpInfoByUserId(Long empId);
}
