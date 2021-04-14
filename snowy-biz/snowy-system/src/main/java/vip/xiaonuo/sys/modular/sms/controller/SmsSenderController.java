package vip.xiaonuo.sys.modular.sms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import vip.xiaonuo.common.annotion.BusinessLog;
import vip.xiaonuo.common.annotion.Permission;
import vip.xiaonuo.common.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.sms.enums.SmsVerifyEnum;
import vip.xiaonuo.sys.modular.sms.param.SysSmsInfoParam;
import vip.xiaonuo.sys.modular.sms.param.SysSmsSendParam;
import vip.xiaonuo.sys.modular.sms.param.SysSmsVerifyParam;
import vip.xiaonuo.sys.modular.sms.service.SmsSenderService;
import vip.xiaonuo.sys.modular.sms.service.SysSmsInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 短信发送控制器
 *
 * @author yubaoshan
 * @date 2020/6/7 16:07
 */
@RestController
@RequestMapping
public class SmsSenderController {

    @Resource
    private SmsSenderService smsSenderService;

    @Resource
    private SysSmsInfoService sysSmsInfoService;

    /**
     * 发送记录查询
     *
     * @author xuyuxiang
     * @date 2020/7/2 12:03
     */
    @Permission
    @GetMapping("/sms/page")
    @BusinessLog(title = "短信发送记录查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData page(SysSmsInfoParam sysSmsInfoParam) {
        return new SuccessResponseData(sysSmsInfoService.page(sysSmsInfoParam));
    }

    /**
     * 发送验证码短信
     *
     * @author yubaoshan
     * @date 2020/6/7 16:07
     */
    @Permission
    @PostMapping("/sms/sendLoginMessage")
    @BusinessLog(title = "发送验证码短信")
    public ResponseData sendLoginMessage(@RequestBody @Validated SysSmsSendParam sysSmsSendParam) {

        // 设置模板中的参数
        HashMap<String, Object> paramMap = CollectionUtil.newHashMap();
        paramMap.put("code", RandomUtil.randomNumbers(6));
        sysSmsSendParam.setParams(paramMap);

        return new SuccessResponseData(smsSenderService.sendShortMessage(sysSmsSendParam));
    }

    /**
     * 验证短信验证码
     *
     * @author yubaoshan
     * @date 2020/6/7 16:07
     */
    @Permission
    @PostMapping("/sms/validateMessage")
    @BusinessLog(title = "验证短信验证码")
    public ResponseData validateMessage(@RequestBody @Validated SysSmsVerifyParam sysSmsVerifyParam) {
        SmsVerifyEnum smsVerifyEnum = smsSenderService.verifyShortMessage(sysSmsVerifyParam);
        return new SuccessResponseData(smsVerifyEnum);
    }

}
