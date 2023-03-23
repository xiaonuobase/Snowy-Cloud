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
package vip.xiaonuo.gateway.core.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @description 转发认证全局过滤器，为请求添加 TOKEN
 * @author dongxiayu
 * @date 2022/11/18 1:51
 */
@Component
public class ForwardAuthFilter implements GlobalFilter {

    private static final String HEADER_ORIGIN = "Origin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();

        /** token **/
        SaTokenConfig saTokenConfig = SaManager.getConfig();
        String tokenName = saTokenConfig.getTokenName();
        List<String> tokenValueList = httpHeaders.get(tokenName);
        String tokenValue = null;
        if(Objects.nonNull(tokenValueList) && !tokenValueList.isEmpty()){
            tokenValue = tokenValueList.get(0);
        }

        /** Origin **/
        String originValue = null;
        List<String> originValueList = httpHeaders.get(HEADER_ORIGIN);
        if(Objects.nonNull(originValueList) && !originValueList.isEmpty()){
            originValue = originValueList.get(0);
        }

        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                // 为请求追加 Origin 参数
                .header(HEADER_ORIGIN, originValue)
                // 为请求追加 Token 参数
                .header(tokenName, tokenValue)
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }

}
