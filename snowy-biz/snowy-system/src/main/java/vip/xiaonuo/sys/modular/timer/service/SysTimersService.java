package vip.xiaonuo.sys.modular.timer.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.timer.entity.SysTimers;
import vip.xiaonuo.sys.modular.timer.param.SysTimersParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 定时任务 服务类
 *
 * @author yubaoshan
 * @date 2020/6/30 18:26
 */
public interface SysTimersService extends IService<SysTimers> {

    /**
     * 分页查询定时任务
     *
     * @param sysTimersParam 查询参数
     * @return 查询分页结果
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    PageResult<SysTimers> page(SysTimersParam sysTimersParam);

    /**
     * 查询所有定时任务
     *
     * @param sysTimersParam 查询参数
     * @return 定时任务列表
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    List<SysTimers> list(SysTimersParam sysTimersParam);

    /**
     * 添加定时任务
     *
     * @param sysTimersParam 添加参数
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    void add(SysTimersParam sysTimersParam);

    /**
     * 删除定时任务
     *
     * @param sysTimersParam 删除参数
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    void delete(SysTimersParam sysTimersParam);

    /**
     * 编辑定时任务
     *
     * @param sysTimersParam 编辑参数
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    void edit(SysTimersParam sysTimersParam);

    /**
     * 查看详情定时任务
     *
     * @param sysTimersParam 查看参数
     * @return 定时任务
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    SysTimers detail(SysTimersParam sysTimersParam);

    /**
     * 启动任务
     *
     * @param sysTimersParam 启动参数
     * @author yubaoshan
     * @date 2020/7/1 14:36
     */
    void start(SysTimersParam sysTimersParam);

    /**
     * 停止任务
     *
     * @param sysTimersParam 停止参数
     * @author yubaoshan
     * @date 2020/7/1 14:36
     */
    void stop(SysTimersParam sysTimersParam);

    /**
     * 获取所有可执行的任务列表
     *
     * @return TimerTaskRunner的所有子类名称集合
     * @author yubaoshan
     * @date 2020/7/1 14:36
     */
    List<String> getActionClasses();

}
