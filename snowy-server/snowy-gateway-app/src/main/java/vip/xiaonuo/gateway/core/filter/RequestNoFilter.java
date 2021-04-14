package vip.xiaonuo.gateway.core.filter;

import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.gateway.core.consts.GatewayFilterOrdered;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * 对请求生成唯一编码
 *
 * @author dongxiayu
 * @date 2021-03-12 23:30
 */
public class RequestNoFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return GatewayFilterOrdered.REQUEST_NO_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 生成唯一请求号uuid
        String requestNo = UUID.randomUUID().toString();

        //增加请求头中的请求号
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header(CommonConstant.REQUEST_NO_HEADER_NAME, requestNo)
                .build();

        // 增加响应头的请求号
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(CommonConstant.REQUEST_NO_HEADER_NAME, requestNo);


        // 放开请求
        return chain.filter(exchange.mutate().request(request).response(response).build());
    }

}
