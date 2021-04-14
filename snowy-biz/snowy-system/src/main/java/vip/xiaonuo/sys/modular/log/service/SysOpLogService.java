package vip.xiaonuo.sys.modular.log.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.log.entity.SysOpLog;
import vip.xiaonuo.sys.modular.log.param.SysOpLogParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统操作日志service接口
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:21
 */
public interface SysOpLogService extends IService<SysOpLog> {

    /**
     * 查询系统操作日志
     *
     * @param sysOpLogParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/30 10:32
     */
    PageResult<SysOpLog> page(SysOpLogParam sysOpLogParam);

    /**
     * 清空系统操作日志
     *
     * @author xuyuxiang
     * @date 2020/6/1 11:05
     */
    void delete();
}
