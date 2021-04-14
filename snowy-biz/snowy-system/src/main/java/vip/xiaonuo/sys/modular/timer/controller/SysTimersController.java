package vip.xiaonuo.sys.modular.timer.controller;

import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.timer.param.SysTimersParam;
import vip.xiaonuo.sys.modular.timer.service.SysTimersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务 控制器
 *
 * @author yubaoshan
 * @date 2020/6/30 18:26
 */
@RestController
public class SysTimersController {

    @Resource
    private SysTimersService sysTimersService;

    /**
     * 分页查询定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/page")
    @BusinessLog(title = "定时任务_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(SysTimersParam sysTimersParam) {
        return new SuccessResponseData(sysTimersService.page(sysTimersParam));
    }

    /**
     * 获取全部定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/list")
    @BusinessLog(title = "定时任务_查询所有", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysTimersParam sysTimersParam) {
        return new SuccessResponseData(sysTimersService.list(sysTimersParam));
    }

    /**
     * 查看详情定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/detail")
    @BusinessLog(title = "定时任务_查看详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysTimersParam.detail.class) SysTimersParam sysTimersParam) {
        return new SuccessResponseData(sysTimersService.detail(sysTimersParam));
    }

    /**
     * 添加定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/add")
    @BusinessLog(title = "定时任务_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysTimersParam.add.class) SysTimersParam sysTimersParam) {
        sysTimersService.add(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 删除定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/delete")
    @BusinessLog(title = "定时任务_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysTimersParam.delete.class) SysTimersParam sysTimersParam) {
        sysTimersService.delete(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 编辑定时任务
     *
     * @author yubaoshan
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/edit")
    @BusinessLog(title = "定时任务_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysTimersParam.edit.class) SysTimersParam sysTimersParam) {
        sysTimersService.edit(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 获取系统的所有任务列表
     *
     * @author yubaoshan
     * @date 2020/7/1 14:34
     */
    @PostMapping("/sysTimers/getActionClasses")
    @BusinessLog(title = "定时任务_任务列表", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData getActionClasses() {
        List<String> actionClasses = sysTimersService.getActionClasses();
        return new SuccessResponseData(actionClasses);
    }

    /**
     * 启动定时任务
     *
     * @author yubaoshan
     * @date 2020/7/1 14:34
     */
    @PostMapping("/sysTimers/start")
    @BusinessLog(title = "定时任务_启动", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData start(@RequestBody @Validated(SysTimersParam.start.class) SysTimersParam sysTimersParam) {
        sysTimersService.start(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 停止定时任务
     *
     * @author yubaoshan
     * @date 2020/7/1 14:34
     */
    @PostMapping("/sysTimers/stop")
    @BusinessLog(title = "定时任务_关闭", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData stop(@RequestBody @Validated(SysTimersParam.stop.class) SysTimersParam sysTimersParam) {
        sysTimersService.stop(sysTimersParam);
        return new SuccessResponseData();
    }

}
