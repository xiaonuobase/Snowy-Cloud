package vip.xiaonuo.security.filter.security.entrypoint;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import vip.xiaonuo.cache.ResourceCache;
import vip.xiaonuo.common.exception.ServiceException;
import vip.xiaonuo.common.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.common.exception.enums.PermissionExceptionEnum;
import vip.xiaonuo.common.exception.enums.ServerExceptionEnum;
import vip.xiaonuo.core.util.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * 未认证用户访问须授权资源端点
 *
 * @author xuyuxiang
 * @date 2020/3/18 11:52
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final Log log = Log.get();

    @Resource
    private ResourceCache resourceCache;

    /**
     * 访问未经授权的接口时执行此方法，未经授权的接口包含系统中存在和不存在的接口，分别处理
     *
     * @author xuyuxiang
     * @date 2020/3/18 19:15
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {

        String requestUri = request.getRequestURI();

        //1.检查redis中RESOURCE缓存是否为空，如果为空，直接抛出系统异常，缓存url作用详见ResourceCollectListener
        Collection<String> urlCollections = resourceCache.getAllResources();
        if (ObjectUtil.isEmpty(urlCollections)) {
            log.error(">>> 获取缓存的Resource Url为空，请检查缓存中是否被误删，requestUri={}", requestUri);
            ResponseUtil.responseExceptionError(response,
                    ServerExceptionEnum.SERVER_ERROR.getCode(),
                    ServerExceptionEnum.SERVER_ERROR.getMessage(),
                    new ServiceException(ServerExceptionEnum.SERVER_ERROR).getStackTrace()[0].toString());
            return;
        }

        //2.判断缓存的url中是否有当前请求的uri，没有的话响应给前端404
        if (!urlCollections.contains(requestUri)) {
            log.error(">>> 当前请求的uri不存在，请检查请求地址是否正确或缓存中是否被误删，requestUri={}", requestUri);
            ResponseUtil.responseExceptionError(response,
                    PermissionExceptionEnum.URL_NOT_EXIST.getCode(),
                    PermissionExceptionEnum.URL_NOT_EXIST.getMessage(),
                    new ServiceException(PermissionExceptionEnum.URL_NOT_EXIST).getStackTrace()[0].toString());
            return;
        }

        //3.响应给前端无权限访问本接口（没有携带token）
        log.error(">>> 没有权限访问该资源，requestUri={}", requestUri);
        ResponseUtil.responseExceptionError(response,
                AuthExceptionEnum.REQUEST_TOKEN_EMPTY.getCode(),
                AuthExceptionEnum.REQUEST_TOKEN_EMPTY.getMessage(),
                new ServiceException(AuthExceptionEnum.REQUEST_TOKEN_EMPTY).getStackTrace()[0].toString());
    }
}
