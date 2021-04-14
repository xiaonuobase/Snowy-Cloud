package vip.xiaonuo.sys.modular.monitor.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.monitor.param.SysOnlineUserParam;
import vip.xiaonuo.sys.modular.monitor.result.SysOnlineUserResult;

/**
 * 系统在线用户service接口
 *
 * @author xuyuxiang
 * @date 2020/4/7 17:06
 */
public interface SysOnlineUserService {

    /**
     * 系统在线用户列表
     *
     * @param sysOnlineUserParam 查询参数
     * @return 在线用户列表
     * @author xuyuxiang
     * @date 2020/4/7 17:09
     */
    PageResult<SysOnlineUserResult> list(SysOnlineUserParam sysOnlineUserParam);

    /**
     * 系统在线用户强退
     *
     * @param sysOnlineUserParam 操作参数
     * @author xuyuxiang
     * @date 2020/4/7 20:20
     */
    void forceExist(SysOnlineUserParam sysOnlineUserParam);
}
