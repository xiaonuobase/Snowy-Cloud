package vip.xiaonuo.auth.api;

import vip.xiaonuo.common.pojo.CommonResult;

/**
 * AuthInnerApi
 *
 * @author dongxiayu
 * @date 2024/1/12 23:28
 */
public interface AuthInnerApi {

    /**
     * B端账号密码内部环境登录
     *
     * @author dongxiayu
     * @date 2021/10/15 13:12
     **/
    CommonResult<String> doLogin(String username, String password);

    /**
     * B端账号密码内部环境使用token登出
     *
     * @author dongxiayu
     * @date 2021/10/15 13:12
     **/
    CommonResult<String> doLogout(String token);
}
