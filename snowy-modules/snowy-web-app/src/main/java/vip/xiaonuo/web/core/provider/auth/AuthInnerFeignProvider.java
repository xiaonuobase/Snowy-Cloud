package vip.xiaonuo.web.core.provider.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.auth.feign.AuthInnerFeign;
import vip.xiaonuo.auth.modular.login.provider.AuthInnerProvider;
import vip.xiaonuo.common.pojo.CommonResult;

/**
 * AuthInnerFeignProvider
 *
 * @author dongxiayu
 * @date 2024/1/12 23:23
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthInnerFeignProvider implements AuthInnerFeign {

    private final AuthInnerProvider authInnerProvider;

    /**
     * B端账号密码内部环境登录
     *
     * @param username
     * @param password
     * @author dongxiayu
     * @date 2021/10/15 13:12
     */
    @Override
    public CommonResult<String> doLogin(String username, String password) {
        return authInnerProvider.doLogin(username, password);
    }

    /**
     * B端账号密码内部环境登出
     *
     * @param token
     * @author dongxiayu
     * @date 2021/10/15 13:12
     */
    @Override
    public CommonResult<String> doLogout(String token) {
        return authInnerProvider.doLogout(token);
    }
}