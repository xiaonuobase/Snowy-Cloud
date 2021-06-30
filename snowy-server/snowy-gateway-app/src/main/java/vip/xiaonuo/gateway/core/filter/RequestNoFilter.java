/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.gateway.core.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.gateway.core.consts.GatewayFilterOrdered;

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
