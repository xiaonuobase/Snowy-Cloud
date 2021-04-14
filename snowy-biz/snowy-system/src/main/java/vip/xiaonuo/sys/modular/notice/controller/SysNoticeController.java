package vip.xiaonuo.sys.modular.notice.controller;

import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.annotion.Permission;
import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.notice.param.SysNoticeParam;
import vip.xiaonuo.sys.modular.notice.service.SysNoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统通知公告控制器
 *
 * @author xuyuxiang
 * @date 2020/6/28 17:18
 */
@RestController
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;

    /**
     * 查询系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:24
     */
    @Permission
    @GetMapping("/sysNotice/page")
    @BusinessLog(title = "系统通知公告_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(SysNoticeParam sysNoticeParam) {
        return new SuccessResponseData(sysNoticeService.page(sysNoticeParam));
    }

    /**
     * 查询我收到的系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/29 11:59
     */
    @Permission
    @GetMapping("/sysNotice/received")
    @BusinessLog(title = "系统通知公告_已收", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData received(SysNoticeParam sysNoticeParam) {
        return new SuccessResponseData(sysNoticeService.received(sysNoticeParam));
    }

    /**
     * 添加系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:24
     */
    @Permission
    @PostMapping("/sysNotice/add")
    @BusinessLog(title = "系统通知公告_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysNoticeParam.add.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.add(sysNoticeParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @PostMapping("/sysNotice/delete")
    @BusinessLog(title = "系统通知公告_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysNoticeParam.delete.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.delete(sysNoticeParam);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @PostMapping("/sysNotice/edit")
    @BusinessLog(title = "系统通知公告_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysNoticeParam.edit.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.edit(sysNoticeParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @GetMapping("/sysNotice/detail")
    @BusinessLog(title = "系统通知公告_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysNoticeParam.detail.class) SysNoticeParam sysNoticeParam) {
        return new SuccessResponseData(sysNoticeService.detail(sysNoticeParam));
    }

    /**
     * 修改状态
     *
     * @author xuyuxiang
     * @date 2020/6/29 9:44
     */
    @Permission
    @PostMapping("/sysNotice/changeStatus")
    @BusinessLog(title = "系统通知公告_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
    public ResponseData changeStatus(@RequestBody @Validated(SysNoticeParam.changeStatus.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.changeStatus(sysNoticeParam);
        return new SuccessResponseData();
    }
}
