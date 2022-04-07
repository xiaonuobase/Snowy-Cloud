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
package vip.xiaonuo.sys.config;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.consts.FeignConstant;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger配置
 *
 * @author xuyuxiang
 * 加入分组功能(默认注释掉)
 * <p>
 * https://doc.xiaominfo.com/knife4j/changelog/2017-12-18-swagger-bootstrap-ui-1.7-issue.html
 * </p>
 * @author ldw4033#163.com
 * @date 2021/4/9 10:42
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private List<RequestParameter> getParameters() {
        RequestParameterBuilder tokenBuilder = new RequestParameterBuilder();
        tokenBuilder
                .name("Authorization")
                .description("token令牌")
                .required(false)
                .in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON))
                .build();
        return Collections.singletonList(tokenBuilder.build());
    }

    @Bean
    public Docket defaultApi() {
        List<RequestParameter> parameters = getParameters();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(defaultApiInfo())
                .groupName(FeignConstant.MAIN_APP)
                .select()
                .apis(RequestHandlerSelectors.basePackage(CommonConstant.DEFAULT_PACKAGE_NAME))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(parameters);
    }

    private ApiInfo defaultApiInfo() {
        return new ApiInfoBuilder()
                .title(FeignConstant.MAIN_APP+" Doc")
                .description(FeignConstant.MAIN_APP+" Doc文档")
                .termsOfServiceUrl("https://www.xiaonuo.vip")
                .contact(new Contact("xuyuxiang, yubaoshan, dongxiayu", "https://www.xiaonuo.vip", ""))
                .version("1.7.0")
                .build();
    }

    /**
     * 想分组请放开注释
     */

    // @Bean
    // public Docket groupRestApi() {
    //     List<Parameter> parameters = getParameters();
    //     return new Docket(DocumentationType.SWAGGER_2)
    //             .apiInfo(groupApiInfo())
    //             .groupName("自定义")
    //             .select()
    //             //TODO 这里改为自己的包名
    //             .apis(RequestHandlerSelectors.basePackage("com.example.XXX"))
    //             .paths(PathSelectors.any())
    //             .build()
    //             .globalOperationParameters(parameters);
    // }
    //
    // private ApiInfo groupApiInfo() {
    //     return new ApiInfoBuilder()
    //             .title("自定义")
    //             .description("自定义API")
    //             .termsOfServiceUrl("http://www.example.com/")
    //             .version("1.0")
    //             .build();
    // }

    /**
     * 方案1
     * @description https://blog.csdn.net/weixin_49523761/article/details/122305980
     * @author dongxiayu
     * @date 2022/3/16 3:33
     **/
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
                    List<T> mappings) {
                List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectUtil.getField(bean.getClass(), "handlerMappings");

                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                }
                catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    /**
     * 方案2
     * @description https://blog.csdn.net/qq_33547169/article/details/122842729
     * @author dongxiayu
     * @date 2022/3/16 3:32
     **/
//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
//            WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
//            ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
//            CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = webEndpointProperties.getDiscovery().isEnabled() &&
//                (org.springframework.util.StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
//    }

}
