package vip.xiaonuo.sys.modular.notice.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.notice.entity.SysNotice;
import vip.xiaonuo.sys.modular.notice.param.SysNoticeParam;
import vip.xiaonuo.sys.modular.notice.result.SysNoticeDetailResult;
import vip.xiaonuo.sys.modular.notice.result.SysNoticeReceiveResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统通知公告service接口
 *
 * @author xuyuxiang
 * @date 2020/6/28 17:16
 */
public interface SysNoticeService extends IService<SysNotice> {

    /**
     * 查询系统通知公告
     *
     * @param sysNoticeParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/6/28 17:16
     */
    PageResult<SysNotice> page(SysNoticeParam sysNoticeParam);

    /**
     * 添加系统通知公告
     *
     * @param sysNoticeParam 添加参数
     * @author xuyuxiang
     * @date 2020/6/28 17:21
     */
    void add(SysNoticeParam sysNoticeParam);

    /**
     * 删除系统通知公告
     *
     * @param sysNoticeParam 删除参数
     * @author xuyuxiang
     * @date 2020/6/28 17:22
     */
    void delete(SysNoticeParam sysNoticeParam);

    /**
     * 编辑系统通知公告
     *
     * @param sysNoticeParam 编辑参数
     * @author xuyuxiang
     * @date 2020/6/28 17:22
     */
    void edit(SysNoticeParam sysNoticeParam);

    /**
     * 查看系统通知公告
     *
     * @param sysNoticeParam 查看参数
     * @return 通知公告详情结果
     * @author xuyuxiang
     * @date 2020/6/28 17:22
     */
    SysNoticeDetailResult detail(SysNoticeParam sysNoticeParam);

    /**
     * 修改状态
     *
     * @param sysNoticeParam 修改参数
     * @author xuyuxiang
     * @date 2020/6/29 9:45
     */
    void changeStatus(SysNoticeParam sysNoticeParam);

    /**
     * 查询当前登陆用户已收通知公告
     *
     * @param sysNoticeParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/6/29 12:01
     */
    PageResult<SysNoticeReceiveResult> received(SysNoticeParam sysNoticeParam);
}
