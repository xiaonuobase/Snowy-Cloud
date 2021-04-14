package vip.xiaonuo.sys.modular.timer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.Log;
import vip.xiaonuo.common.exception.ServiceException;
import vip.xiaonuo.common.timer.TimerTaskRunner;
import vip.xiaonuo.sys.modular.timer.enums.exp.SysTimersExceptionEnum;
import vip.xiaonuo.sys.modular.timer.service.TimerExeService;
import org.springframework.stereotype.Service;

/**
 * hutool方式的定时任务执行
 *
 * @author yubaoshan
 * @date 2020/7/1 13:48
 */
@Service
public class HutoolTimerExeServiceImpl implements TimerExeService {

    private static final Log log = Log.get();

    @Override
    public void startTimer(String taskId, String cron, String className) {

        if (ObjectUtil.hasEmpty(taskId, cron, className)) {
            throw new ServiceException(SysTimersExceptionEnum.EXE_EMPTY_PARAM);
        }

        // 预加载类看是否存在此定时任务类
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServiceException(SysTimersExceptionEnum.TIMER_NOT_EXISTED);
        }

        // 定义hutool的任务
        Task task = () -> {
            try {
                TimerTaskRunner timerTaskRunner = (TimerTaskRunner) SpringUtil.getBean(Class.forName(className));
                timerTaskRunner.action();
            } catch (ClassNotFoundException e) {
                log.error(">>> 任务执行异常：{}", e.getMessage());
            }
        };

        // 开始执行任务
        CronUtil.schedule(taskId, cron, task);
    }

    @Override
    public void stopTimer(String taskId) {
        CronUtil.remove(taskId);
    }

}
