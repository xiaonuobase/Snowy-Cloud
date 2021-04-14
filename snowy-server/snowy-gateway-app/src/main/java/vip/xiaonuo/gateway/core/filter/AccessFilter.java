package vip.xiaonuo.gateway.core.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.consts.SymbolConstant;
import vip.xiaonuo.common.exception.AuthException;
import vip.xiaonuo.common.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.common.exception.enums.PermissionExceptionEnum;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.gateway.core.auth.impl.TokenServiceImpl;
import vip.xiaonuo.gateway.core.consumer.PermissionServiceApiConsumer;
import vip.xiaonuo.gateway.core.enums.AdminTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author : dongxiayu
 * @classname : AccessFilter
 * @description : 网关访问过滤
 * 增加网关层统一用户token认证处理，业务组件不再校验用户会话有效性
 * @date 2021-03-12 23:30
 */
@Slf4j
public class AccessFilter implements GlobalFilter {

    @Value("${snowy.gate.ignore.startWith}")
    private String startWith;

    private static final String GATE_WAY_PREFIX = "/api";

    @Autowired
    private TokenServiceImpl tokenService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        LinkedHashSet requiredAttribute = serverWebExchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = serverWebExchange.getRequest();
        // 获取当前网关访问的URI
        String requestUri = request.getPath().pathWithinApplication().value();
        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()) {
                URI next = iterator.next();
                if (next.getPath().startsWith(GATE_WAY_PREFIX)) {
                    requestUri = next.getPath().substring(GATE_WAY_PREFIX.length());
                }
            }
        }

        final String method = request.getMethod().toString();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 网关不进行拦截的URI配置，常见如验证码、Login接口
        if (isStartWith(requestUri)) {
            ServerHttpRequest build = mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }
        //获取请求头的Auth token
        String authToken = request.getHeaders().getFirst(CommonConstant.AUTHORIZATION);
        authToken = authToken.substring(CommonConstant.TOKEN_TYPE_BEARER.length() + 1);

        //当前请求的路径
        String currentRequestPath = request.getPath().toString();
        String currentRequestUri = request.getURI().toString();

        // 并获取当前登录用户信息
        SysLoginUser sysLoginUser = null;
        try {
            // 判断用户token，获取用户信息
            sysLoginUser = tokenService.getLoginUserByToken(authToken);

        } catch (AuthException e) {
        	e.printStackTrace();
            log.error(">>> 用户Token过期异常", e);
            return getVoidMono(serverWebExchange, ResponseData.error(e.getCode(),e.getErrorMessage()), HttpStatus.UNAUTHORIZED);
        }

        // 判断用户资源权限
        if(sysLoginUser==null){
            log.error(">>> 用户认证信息不存在");
            return getVoidMono(serverWebExchange, ResponseData.error(AuthExceptionEnum.NO_LOGIN_USER.getCode(),AuthExceptionEnum.NO_LOGIN_USER.getMessage()), HttpStatus.FORBIDDEN);
        }

        // 如果是超级管理员，直接放过资源权限校验
        boolean isSuperAdmin = isAdmin(sysLoginUser,AdminTypeEnum.SUPER_ADMIN.getCode());
        if(isSuperAdmin) {
            ServerHttpRequest build = mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }

        // 首先校验当前用户有没有 当前请求requestUri的权限
        boolean hasUriPermission = hasPermission(sysLoginUser,currentRequestPath);
        if (!hasUriPermission) {
            // 当前用户具有资源访问权限
            log.error(">>> 当前用户没有资源访问权限->"+currentRequestUri);
            return getVoidMono(serverWebExchange, ResponseData.error(PermissionExceptionEnum.NO_PERMISSION.getCode(),PermissionExceptionEnum.NO_PERMISSION.getMessage()), HttpStatus.FORBIDDEN);
        }

        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, ResponseData body, HttpStatus status) {
        serverWebExchange.getResponse().setStatusCode(status);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    /**
     * URI是否以什么打头
     * @author dongxiayu
     * @date 2021/3/17 11:43
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 判断当前登录用户是否是指定类型的管理员
     *
     * @author dongxiayu
     * @date 2021/3/17 11:43
     */
    private boolean isAdmin(SysLoginUser sysLoginUser,Integer adminTypeCode) {
        Integer adminType = sysLoginUser.getAdminType();
        boolean flag = false;
        if (adminType.equals(adminTypeCode)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断当前登录用户是否是否包含资源权限
     * 1.获取系统所有权限
     * 2.判断当前uri是否在所有权限验证清单，如果不存在则无需进行用户验证
     * 3.如果uri存在于权限验证清单，则进行用户权限匹配，如果无法匹配则返回错误
     * @author dongxiayu
     * @date 2021/3/17 11:43
     */
    public boolean hasPermission(SysLoginUser sysLoginUser,String requestUri) {
        String removePrefix = StrUtil.removePrefix(requestUri, SymbolConstant.LEFT_DIVIDE);
        String requestPermission = removePrefix.replaceAll(SymbolConstant.LEFT_DIVIDE, SymbolConstant.COLON);

        PermissionServiceApiConsumer permissionServiceApiConsumer = SpringUtil.getBean(PermissionServiceApiConsumer.class);
        List<String> allPermissionList = permissionServiceApiConsumer.getAllPermission();
        if(CollectionUtil.isEmpty(allPermissionList)){
            return true;
        }

        if(!allPermissionList.contains(requestPermission)){
            return true;
        }

        return sysLoginUser.getPermissions().contains(requestPermission);
    }


}
