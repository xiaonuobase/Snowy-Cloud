package vip.xiaonuo.sys.modular.emp.service;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.sys.modular.emp.entity.SysEmpPos;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 员工职位service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 15:10
 */
public interface SysEmpPosService extends IService<SysEmpPos> {

    /**
     * 保存职位相关信息
     *
     * @param empId     员工id（用户id）
     * @param posIdList 职位id集合
     * @author xuyuxiang
     * @date 2020/4/2 9:00
     */
    void addOrEdit(Long empId, List<Long> posIdList);

    /**
     * 获取所属职位信息
     *
     * @param empId    员工id（用户id）
     * @param isFillId 是否需要返回id信息
     * @return 增强版hashMap，格式：[{"posId":456, "posCode":"zjl", "posName":"总经理"}]
     * @author xuyuxiang
     * @date 2020/4/2 20:07
     */
    List<Dict> getEmpPosDictList(Long empId, boolean isFillId);

    /**
     * 根据职位id判断该职位下是否有员工
     *
     * @param posId 职位id
     * @return 该职位下是否有员工，true是，false否
     * @author xuyuxiang
     * @date 2020/6/23 10:40
     */
    boolean hasPosEmp(Long posId);

    /**
     * 根据员工id删除对用的员工-职位信息
     *
     * @param empId 员工id（用户id）
     * @author xuyuxiang
     * @date 2020/6/28 14:58
     */
    void deleteEmpPosInfoByUserId(Long empId);
}
