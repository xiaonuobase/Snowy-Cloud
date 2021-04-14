package vip.xiaonuo.sys.modular.app.controller;

import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.annotion.Permission;
import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.app.param.SysAppParam;
import vip.xiaonuo.sys.modular.app.service.SysAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统应用控制器
 *
 * @author xuyuxiang
 * @date 2020/3/20 21:25
 */
@RestController
public class SysAppController {

    @Resource
    private SysAppService sysAppService;

    /**
     * 查询系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/20 21:23
     */
    @Permission
    @GetMapping("/sysApp/page")
    @BusinessLog(title = "系统应用_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(SysAppParam sysAppParam) {
        return new SuccessResponseData(sysAppService.page(sysAppParam));
    }

    /**
     * 添加系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:44
     */
    @Permission
    @PostMapping("/sysApp/add")
    @BusinessLog(title = "系统应用_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysAppParam.add.class) SysAppParam sysAppParam) {
        sysAppService.add(sysAppParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @PostMapping("/sysApp/delete")
    @BusinessLog(title = "系统应用_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysAppParam.delete.class) SysAppParam sysAppParam) {
        sysAppService.delete(sysAppParam);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @PostMapping("/sysApp/edit")
    @BusinessLog(title = "系统应用_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysAppParam.edit.class) SysAppParam sysAppParam) {
        sysAppService.edit(sysAppParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/26 9:49
     */
    @Permission
    @GetMapping("/sysApp/detail")
    @BusinessLog(title = "系统应用_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysAppParam.detail.class) SysAppParam sysAppParam) {
        return new SuccessResponseData(sysAppService.detail(sysAppParam));
    }

    /**
     * 系统应用列表
     *
     * @author xuyuxiang
     * @date 2020/4/19 14:55
     */
    @Permission
    @GetMapping("/sysApp/list")
    @BusinessLog(title = "系统应用_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysAppParam sysAppParam) {
        return new SuccessResponseData(sysAppService.list(sysAppParam));
    }

    /**
     * 设为默认应用
     *
     * @author xuyuxiang
     * @date 2020/6/29 16:49
     */
    @Permission
    @PostMapping("/sysApp/setAsDefault")
    @BusinessLog(title = "系统应用_设为默认应用", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData setAsDefault(@RequestBody @Validated(SysAppParam.detail.class) SysAppParam sysAppParam) {
        sysAppService.setAsDefault(sysAppParam);
        return new SuccessResponseData();
    }
}
