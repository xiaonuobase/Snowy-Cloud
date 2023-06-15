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
package vip.xiaonuo.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import vip.xiaonuo.auth.core.enums.SaClientTypeEnum;
import vip.xiaonuo.auth.core.util.StpClientLoginUserUtil;
import vip.xiaonuo.auth.core.util.StpClientUtil;
import vip.xiaonuo.auth.core.util.StpLoginUserUtil;
import vip.xiaonuo.common.enums.SysBuildInEnum;
import vip.xiaonuo.gateway.core.util.GlobalExceptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : dongxiayu
 * @classname : GatewayConfig
 * @description : 网关配置
 * @date : 2022/11/16 15:32
 */
@Import({cn.hutool.extra.spring.SpringUtil.class})
@Configuration
public class GatewayConfigure {

    /**
     * 无需登录的接口地址集合
     */
    private static final String[] NO_LOGIN_PATH_ARR = {
            /* 主入口 */
            "/",

            /* 静态资源 */
            "/favicon.ico",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/configuration/ui",
            "/configuration/security",
            "/ureport/**",
            "/druid/**",
            "/images/**",

            "/api/webapp/v2/api-docs",
            "/api/bizapp/v2/api-docs",

            /* 认证相关 */
            "/api/webapp/auth/c/getPicCaptcha",
            "/api/webapp/auth/c/getPhoneValidCode",
            "/api/webapp/auth/c/doLogin",
            "/api/webapp/auth/c/doLoginByPhone",

            "/api/webapp/auth/b/getPicCaptcha",
            "/api/webapp/auth/b/getPhoneValidCode",
            "/api/webapp/auth/b/doLogin",
            "/api/webapp/auth/b/doLoginByPhone",

            /* 三方登录相关 */
            "/api/webapp/auth/third/render",
            "/api/webapp/auth/third/callback",

            /* 系统基础配置 */
            "/api/webapp/dev/config/sysBaseList",

            /* 系统字典树 */
            "/api/webapp/dev/dict/tree",

            /* 文件下载 */
            "/api/webapp/dev/file/download",

            /* 用户个人中心相关 */
            "/api/webapp/sys/userCenter/getPicCaptcha",
            "/api/webapp/sys/userCenter/findPasswordGetPhoneValidCode",
            "/api/webapp/sys/userCenter/findPasswordGetEmailValidCode",
            "/api/webapp/sys/userCenter/findPasswordByPhone",
            "/api/webapp/sys/userCenter/findPasswordByEmail",

            /* actuator */
            "/actuator",
            "/actuator/**",
    };

    /**
     * 仅超管使用的接口地址集合
     */
    private static final String[] SUPER_PERMISSION_PATH_ARR = {
            "/api/webapp/auth/session/**",
            "/api/webapp/auth/third/page",
            "/api/webapp/client/user/**",
            "/api/webapp/sys/org/**",
            "/api/webapp/sys/position/**",
            "/api/webapp/sys/button/**",
            "/api/webapp/sys/menu/**",
            "/api/webapp/sys/module/**",
            "/api/webapp/sys/spa/**",
            "/api/webapp/sys/role/**",
            "/api/webapp/sys/user/**",
            "/api/webapp/dev/config/**",
            "/api/webapp/dev/dict/**",
            "/api/webapp/dev/email/page",
            "/api/webapp/dev/email/delete",
            "/api/webapp/dev/email/detail",
            "/api/webapp/dev/file/page",
            "/api/webapp/dev/file/list",
            "/api/webapp/dev/file/delete",
            "/api/webapp/dev/file/detail",
            "/api/webapp/dev/job/**",
            "/api/webapp/dev/log/**",
            "/api/webapp/dev/message/page",
            "/api/webapp/dev/message/delete",
            "/api/webapp/dev/message/detail",
            "/api/webapp/dev/monitor/**",
            "/api/webapp/dev/sms/page",
            "/api/webapp/dev/sms/delete",
            "/api/webapp/dev/sms/detail",
            "/api/webapp/gen/basic/**",
            "/api/webapp/gen/config/**",
            "/api/flwapp/flw/model/**",
            "/api/flwapp/flw/templatePrint/**",
            "/api/flwapp/flw/templateSn/**",
            "/api/bizapp/pay/**",
            "/api/bizapp/urp/**",
            "/api/tenapp/dbs/**",
            "/api/tenapp/ten/"
    };

    /**
     * B端要排除的，相当于C端要认证的
     */
    private static final String[] CLIENT_USER_PERMISSION_PATH_ARR = {
            "/auth/c/**",
            "/client/c/**"
    };


    /**
     * @description Feign解码器
     * @author dongxiayu
     * @date 2022/11/16 1:45
     * @return Decoder
     **/
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    /**
     * @description Feign http 消息转换器
     * @author dongxiayu
     * @date 2022/11/16 1:46
     * @return
     **/
    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());
        return new ObjectFactory<HttpMessageConverters>() {
            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return httpMessageConverters;
            }
        };
    }

    /**
     * @description 消息转换器
     * @author dongxiayu
     * @date 2022/11/16 1:47
     * @return
     **/
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    public class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        GateWayMappingJackson2HttpMessageConverter(){
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
            setSupportedMediaTypes(mediaTypes);
        }
    }

    /**
     * @description loadBlance WebClient构建器
     * @author dongxiayu
     * @date 2022/11/16 1:47
     * @return
     **/
//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClientBuilder() {
//        return WebClient.builder();
//    }

    /**
     * RedisTemplate序列化
     *
     * @author xuyuxiang
     * @date 2022/6/21 17:01
     **/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("stpLogic")
    public StpLogic getStpLogic() {
        // 重写Sa-Token的StpLogic，默认客户端类型为B
        return new StpLogic(SaClientTypeEnum.B.getValue());
    }

    @Bean("stpClientLogic")
    public StpLogic getStpClientLogic() {
        // 重写Sa-Token的StpLogic，默认客户端类型为C
        return new StpLogic(SaClientTypeEnum.C.getValue());
    }

    /**
     * 权限认证接口实现类，集成权限认证功能
     *
     * @author dongxiayu
     * @date 2022/7/7 16:16
     **/
    @Component
    public static class StpInterfaceImpl implements StpInterface {

        /**
         * 返回一个账号所拥有的权限码集合
         */
        @Override
        public List<String> getPermissionList(Object loginId, String loginType) {
            if (SaClientTypeEnum.B.getValue().equals(loginType)) {
                return StpLoginUserUtil.getLoginUser().getPermissionCodeList();
            } else {
                return StpClientLoginUserUtil.getClientLoginUser().getPermissionCodeList();
            }
        }

        /**
         * 返回一个账号所拥有的角色标识集合
         */
        @Override
        public List<String> getRoleList(Object loginId, String loginType) {
            if (SaClientTypeEnum.B.getValue().equals(loginType)) {
                return StpLoginUserUtil.getLoginUser().getRoleCodeList();
            } else {
                return StpClientLoginUserUtil.getClientLoginUser().getRoleCodeList();
            }
        }
    }

    /**
     * @description 注册跨域过滤器
     * @author dongxiayu
     * @date 2022/11/16 1:40
     * @return SaReactorFilter
     **/
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定拦截路由
                .addInclude("/**")

                // 设置鉴权的接口
                .setAuth(r -> {
                    // B端的接口校验B端登录
                    SaRouter.match("/**")
                            // 排除无需登录接口
                            .notMatch(CollectionUtil.newArrayList(NO_LOGIN_PATH_ARR))
                            // 排除C端认证接口
                            .notMatch(CollectionUtil.newArrayList(CLIENT_USER_PERMISSION_PATH_ARR))
                            // 校验B端登录
                            .check(r1 -> StpUtil.checkLogin());

                    // C端的接口校验C端登录
                    SaRouter.match("/**")
                            // 排除无需登录接口
                            .notMatch(CollectionUtil.newArrayList(NO_LOGIN_PATH_ARR))
                            // 匹配C端认证接口
                            .match(CollectionUtil.newArrayList(CLIENT_USER_PERMISSION_PATH_ARR))
                            // 校验C端登录
                            .check(r1 -> StpClientUtil.checkLogin());

                    // B端的超管接口校验B端超管角色
                    SaRouter.match("/**")
                            // 排除无需登录接口
                            .notMatch(CollectionUtil.newArrayList(NO_LOGIN_PATH_ARR))
                            // 匹配超管接口
                            .match(CollectionUtil.newArrayList(SUPER_PERMISSION_PATH_ARR))
                            // 校验B端超管角色
                            .check(r1 -> StpUtil.checkRole(SysBuildInEnum.BUILD_IN_ROLE_CODE.getValue()));
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {

                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()

                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            // .setHeader("X-Frame-Options", "SAMEORIGIN")

                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            // OPTIONS预检请求，不做处理
                            .free(r -> {})
                            .back();
                })

                // 异常处理
                .setError(e -> {
                    // 由于过滤器中抛出的异常不进入全局异常处理，所以必须提供[异常处理函数]来处理[认证函数]里抛出的异常
                    // 在[异常处理函数]里的返回值，将作为字符串输出到前端，此处统一转为JSON输出前端
                    SaResponse saResponse = SaHolder.getResponse();
                    saResponse.setHeader(Header.CONTENT_TYPE.getValue(), ContentType.JSON + ";charset=" +  CharsetUtil.UTF_8);
                    return GlobalExceptionUtil.getCommonResult((Exception) e);
                });
    }

}
