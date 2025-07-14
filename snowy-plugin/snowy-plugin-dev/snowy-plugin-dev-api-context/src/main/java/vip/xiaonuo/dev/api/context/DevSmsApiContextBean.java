package vip.xiaonuo.dev.api.context;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.dev.api.DevSmsApi;
import vip.xiaonuo.dev.feign.DevSmsFeign;

/**
 * DevMessageApi上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevSmsApiContextBean implements DevSmsApi {

    final private DevSmsFeign devSmsFeign;

    /**
     * 动态发送短信（使用系统配置的默认短信引擎）
     *
     * @param phoneNumbers     手机号
     * @param templateCodeOrId 模板id或编码
     * @param paramMap         发送参数
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    @Override
    public void sendDynamicSms(String phoneNumbers, String templateCodeOrId, JSONObject paramMap) {
        this.devSmsFeign.sendDynamicSms(phoneNumbers, templateCodeOrId, paramMap);
    }

    /**
     * 发送短信
     *
     * @param phoneNumbers  手机号码，支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。
     *                      上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     * @param signName      短信服务控制台配置且审核通过的短信签名
     * @param templateCode  短信服务控制台配置且审核通过的模板编码
     * @param templateParam 短信模板变量对应的实际值，JSON格式。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
     * @author xuyuxiang
     * @date 2022/2/24 13:42
     **/
    @Override
    public void sendSmsAliyun(String phoneNumbers, String signName, String templateCode, String templateParam) {
        this.devSmsFeign.sendSmsAliyun(phoneNumbers, signName, templateCode, templateParam);
    }

    /**
     * 发送短信
     *
     * @param phoneNumbers  手机号码，支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。
     *                      上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     * @param signName      短信服务控制台配置且审核通过的短信签名
     * @param templateCode  短信服务控制台配置且审核通过的模板编码
     * @param templateParam 短信模板变量对应的顺序。支持传入多个参数，逗号拼接，示例："张三,15038****76,进行中"}
     * @author xuyuxiang
     * @date 2022/2/24 13:42
     **/
    @Override
    public void sendSmsTencent(String phoneNumbers, String signName, String templateCode, String templateParam) {
        this.devSmsFeign.sendSmsTencent(phoneNumbers, signName, templateCode, templateParam);
    }

    /**
     * 发送短信
     *
     * @param phoneNumbers 手机号码，支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。
     *                     上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     * @param signName     短信服务控制台配置且审核通过的短信签名
     * @param message      短信内容，发送时编写好的整条短信内容，不带签名【】
     * @author yubaoshan
     * @date 2024/5/20 12:00
     **/
    @Override
    public void sendSmsXiaonuo(String phoneNumbers, String signName, String message) {
        this.devSmsFeign.sendSmsXiaonuo(phoneNumbers, signName, message);
    }
}
