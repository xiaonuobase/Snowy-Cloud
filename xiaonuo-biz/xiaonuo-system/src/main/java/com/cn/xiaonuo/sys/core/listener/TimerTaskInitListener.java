package com.cn.xiaonuo.sys.core.listener;

import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cn.xiaonuo.sys.modular.timer.entity.SysTimers;
import com.cn.xiaonuo.sys.modular.timer.enums.TimerJobStatusEnum;
import com.cn.xiaonuo.sys.modular.timer.param.SysTimersParam;
import com.cn.xiaonuo.sys.modular.timer.service.SysTimersService;
import com.cn.xiaonuo.sys.modular.timer.service.TimerExeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : dongxiayu
 * @classname : TimerTaskInitListener
 * @description : 项目定时任务启动的listener
 * @date : 2020/12/14 15:38
 */
@Component
public class TimerTaskInitListener implements ApplicationRunner, Ordered {

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        SysTimersService sysTimersService = SpringUtil.getBean(SysTimersService.class);
        TimerExeService timerExeService = SpringUtil.getBean(TimerExeService.class);
        // 获取所有开启状态的任务
        SysTimersParam sysTimersParam = new SysTimersParam();
        sysTimersParam.setJobStatus(TimerJobStatusEnum.RUNNING.getCode());
        List<SysTimers> list = sysTimersService.list(sysTimersParam);

        // 添加定时任务到调度器
        for (SysTimers sysTimers : list) {
            timerExeService.startTimer(String.valueOf(sysTimers.getId()), sysTimers.getCron(), sysTimers.getActionClass());
        }

        // 设置秒级别的启用
        CronUtil.setMatchSecond(true);

        // 启动定时器执行器
        CronUtil.start();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE-10;
    }
}
