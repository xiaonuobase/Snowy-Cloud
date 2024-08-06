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
package vip.xiaonuo.mocker.token.core.interceptor;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import vip.xiaonuo.common.util.CommonThreadLocalUtil;
import vip.xiaonuo.mocker.token.core.consts.MockerTokenConstant;


/**
 * @author : dongxiayu
 * @classname : JobAddFeignTokenInterceptor
 * @description : JobAddFeignToken请求拦截器
 * @date
 * @Slf4j: 2021/3/28 17:26
 */
@Slf4j
@Configuration
public class MockerTokenFeignAddInterceptor implements RequestInterceptor {

    private static final String GLOBAL_TOKEN_KEY = "Token";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String mockerToken = CommonThreadLocalUtil.get(MockerTokenConstant.MOCKER_TOKEN_Token);
        if(StrUtil.isNotBlank(mockerToken)){
            requestTemplate.header(GLOBAL_TOKEN_KEY, mockerToken);
        }
    }
}