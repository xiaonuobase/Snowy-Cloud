/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.web.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.common.exception.CommonException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Feign错误解码器，将远程服务返回的业务异常还原为CommonException
 *
 * @author yubaoshan
 * @date 2026/10/13 10:00
 */
@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        byte[] body = null;
        try {
            if (ObjectUtil.isNotEmpty(response.body())) {
                body = Util.toByteArray(response.body().asInputStream());
            }
        } catch (IOException e) {
            log.error(">>> 读取Feign异常响应体失败，方法：{}", methodKey, e);
        }
        if (ObjectUtil.isNotEmpty(body)) {
            String bodyStr = new String(body, StandardCharsets.UTF_8);
            // 远程服务全局异常处理返回的是CommonResult，取出原错误码与提示
            if (JSONUtil.isTypeJSONObject(bodyStr)) {
                JSONObject result = JSONUtil.parseObj(bodyStr);
                Integer code = result.getInt("code");
                String msg = result.getStr("msg");
                if (ObjectUtil.isNotEmpty(code) && StrUtil.isNotBlank(msg)) {
                    log.warn(">>> 远程服务返回业务异常，方法：{}，状态码：{}，提示：{}", methodKey, response.status(), msg);
                    return new CommonException(code, msg);
                }
            }
        }
        // 非CommonResult格式（服务不可用、路径不存在等），响应体已读取，需重新放回后交给默认解码器
        return defaultErrorDecoder.decode(methodKey, response.toBuilder().body(body).build());
    }
}
