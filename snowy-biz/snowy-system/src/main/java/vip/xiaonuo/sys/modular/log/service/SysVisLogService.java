package vip.xiaonuo.sys.modular.log.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.log.entity.SysVisLog;
import vip.xiaonuo.sys.modular.log.param.SysVisLogParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统访问日志service接口
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:20
 */
public interface SysVisLogService extends IService<SysVisLog> {

    /**
     * 查询系统访问日志
     *
     * @param sysVisLogParam 查询参数
     * @return 查询结果集合
     * @author xuyuxiang
     * @date 2020/3/24 20:55
     */
    PageResult<SysVisLog> page(SysVisLogParam sysVisLogParam);

    /**
     * 清空系统访问日志
     *
     * @author xuyuxiang
     * @date 2020/6/1 11:04
     */
    void delete();
}
