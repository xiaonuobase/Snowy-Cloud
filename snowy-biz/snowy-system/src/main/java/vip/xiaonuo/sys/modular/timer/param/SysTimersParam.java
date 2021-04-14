package vip.xiaonuo.sys.modular.timer.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 定时任务
 *
 * @author yubaoshan
 * @date 2020/6/30 18:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTimersParam extends BaseParam {

    /**
     * 定时器id
     */
    @NotNull(message = "主键id不能为空，请检查id字段", groups = {edit.class, detail.class, delete.class, start.class, stop.class})
    private Long id;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空，请检查timerName字段", groups = {add.class, edit.class})
    private String timerName;

    /**
     * 执行任务的class的类名（实现了TimerTaskRunner接口的类的全称）
     */
    @NotBlank(message = "任务的class的类名不能为空，请检查actionClass字段", groups = {add.class, edit.class})
    private String actionClass;

    /**
     * 定时任务表达式
     */
    @NotBlank(message = "定时任务表达式不能为空，请检查cron字段", groups = {add.class, edit.class})
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
