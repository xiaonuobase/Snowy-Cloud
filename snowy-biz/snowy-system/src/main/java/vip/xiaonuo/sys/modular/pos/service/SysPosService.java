package vip.xiaonuo.sys.modular.pos.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.pos.entity.SysPos;
import vip.xiaonuo.sys.modular.pos.param.SysPosParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统职位service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 16:00
 */
public interface SysPosService extends IService<SysPos> {

    /**
     * 查询系统职位
     *
     * @param sysPosParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    PageResult<SysPos> page(SysPosParam sysPosParam);

    /**
     * 系统职位列表
     *
     * @param sysPosParam 查询参数
     * @return 职位列表
     * @author yubaoshan
     * @date 2020/6/21 23:44
     */
    List<SysPos> list(SysPosParam sysPosParam);

    /**
     * 添加系统职位
     *
     * @param sysPosParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void add(SysPosParam sysPosParam);

    /**
     * 删除系统职位
     *
     * @param sysPosParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void delete(SysPosParam sysPosParam);

    /**
     * 编辑系统职位
     *
     * @param sysPosParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/25 14:58
     */
    void edit(SysPosParam sysPosParam);

    /**
     * 查看系统职位
     *
     * @param sysPosParam 查看参数
     * @return 系统职位
     * @author xuyuxiang
     * @date 2020/3/26 9:50
     */
    SysPos detail(SysPosParam sysPosParam);
}
