package vip.xiaonuo.sys.modular.file.controller;

import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.annotion.Permission;
import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.file.param.SysFileInfoParam;
import vip.xiaonuo.sys.modular.file.service.SysFileInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件信息表 控制器
 *
 * @author yubaoshan
 * @date 2020/6/7 22:15
 */
@RestController
public class SysFileInfoController {

    @Resource
    private SysFileInfoService sysFileInfoService;

    /**
     * 上传文件
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @PostMapping("/sysFileInfo/upload")
    @BusinessLog(title = "文件信息表_上传文件", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData upload(@RequestPart("file") MultipartFile file) {
        Long fileId = sysFileInfoService.uploadFile(file);
        return new SuccessResponseData(fileId);
    }

    /**
     * 下载文件
     *
     * @author yubaoshan, xuyuxiang
     * @date 2020/6/9 21:53
     */
    @GetMapping("/sysFileInfo/download")
    @BusinessLog(title = "文件信息表_下载文件", opType = LogAnnotionOpTypeEnum.OTHER)
    public void download(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam, HttpServletResponse response) {
        sysFileInfoService.download(sysFileInfoParam, response);
    }

    /**
     * 文件预览
     *
     * @author yubaoshan, xuyuxiang
     * @date 2020/6/9 22:07
     */
    @GetMapping("/sysFileInfo/preview")
    public void preview(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam, HttpServletResponse response) {
        sysFileInfoService.preview(sysFileInfoParam, response);
    }

    /**
     * 分页查询文件信息表
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @Permission
    @GetMapping("/sysFileInfo/page")
    @BusinessLog(title = "文件信息表_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(SysFileInfoParam sysFileInfoParam) {
        return new SuccessResponseData(sysFileInfoService.page(sysFileInfoParam));
    }

    /**
     * 获取全部文件信息表
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @Permission
    @GetMapping("/sysFileInfo/list")
    @BusinessLog(title = "文件信息表_查询所有", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysFileInfoParam sysFileInfoParam) {
        return new SuccessResponseData(sysFileInfoService.list(sysFileInfoParam));
    }

    /**
     * 查看详情文件信息表
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @Permission
    @GetMapping("/sysFileInfo/detail")
    @BusinessLog(title = "文件信息表_查看详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam) {
        return new SuccessResponseData(sysFileInfoService.detail(sysFileInfoParam));
    }

    /**
     * 删除文件信息表
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @Permission
    @PostMapping("/sysFileInfo/delete")
    @BusinessLog(title = "文件信息表_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysFileInfoParam.delete.class) SysFileInfoParam sysFileInfoParam) {
        sysFileInfoService.delete(sysFileInfoParam);
        return new SuccessResponseData();
    }

}
