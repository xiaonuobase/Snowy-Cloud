package vip.xiaonuo.sys.modular.timer.entity;

import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务
 *
 * @author yubaoshan
 * @date 2020/6/30 18:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_timers")
public class SysTimers extends BaseEntity {


    /**
     * 定时器id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务名称
     */
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerTaskRunner接口的类的全称）
     */
    private String actionClass;

    /**
     * 定时任务表达式
     */
    private String cron;

    /**
     * 状态（字典 1运行  2停止）
     */
    private Integer jobStatus;

    /**
     * 备注信息
     */
    private String remark;

}
