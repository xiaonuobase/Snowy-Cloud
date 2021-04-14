package vip.xiaonuo.gateway.core.auth.impl;

import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.cache.UserCache;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.exception.AuthException;
import vip.xiaonuo.common.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.gateway.core.jwt.JwtPayLoad;
import vip.xiaonuo.gateway.core.jwt.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * token相关service实现类
 *
 * @author dongxiayu
 * @date 2020/3/11 16:58
 */
@Service
public class TokenServiceImpl {


    @Resource
    private UserCache userCache;

    public String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader(CommonConstant.AUTHORIZATION);
        if (ObjectUtil.isEmpty(authToken)) {
            return null;
        } else {
            //token不是以Bearer打头，则响应回格式不正确
            if (!authToken.startsWith(CommonConstant.TOKEN_TYPE_BEARER)) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
            try {
                authToken = authToken.substring(CommonConstant.TOKEN_TYPE_BEARER.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
        }

        return authToken;
    }

    public SysLoginUser getLoginUserByToken(String token) {

        //校验token，错误则抛异常
        this.checkToken(token);

        //根据token获取JwtPayLoad部分
        JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);

        //从redis缓存中获取登录用户
        Object cacheObject = userCache.get(jwtPayLoad.getUuid());

        //用户不存在则表示登录已过期
        if (ObjectUtil.isEmpty(cacheObject)) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }

        //转换成登录用户
        SysLoginUser sysLoginUser = (SysLoginUser) cacheObject;

        //用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //返回用户
        return sysLoginUser;
    }

    public void checkToken(String token) {
        //校验token是否正确
        Boolean tokenCorrect = JwtTokenUtil.checkToken(token);
        if (!tokenCorrect) {
            throw new AuthException(AuthExceptionEnum.REQUEST_TOKEN_ERROR);
        }

        //校验token是否失效
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
        if (tokenExpired) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }
    }

    /**
     * 缓存token与登录用户信息对应, 默认2个小时
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:51
     */
    private void cacheLoginUser(JwtPayLoad jwtPayLoad, SysLoginUser sysLoginUser) {
        String redisLoginUserKey = jwtPayLoad.getUuid();
        userCache.put(redisLoginUserKey, sysLoginUser);
    }
}
