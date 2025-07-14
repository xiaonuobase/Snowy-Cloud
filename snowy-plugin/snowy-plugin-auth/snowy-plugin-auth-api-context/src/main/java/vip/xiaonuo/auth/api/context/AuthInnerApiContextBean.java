package vip.xiaonuo.auth.api.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.auth.api.AuthInnerApi;
import vip.xiaonuo.auth.feign.AuthInnerFeign;
import vip.xiaonuo.common.pojo.CommonResult;

/**
 *
 * AuthInnerApi上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInnerApiContextBean implements AuthInnerApi {

    private final AuthInnerFeign authInnerFeign;

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
        return this.authInnerFeign.doLogin(username, password);
    }

    /**
     * B端账号密码内部环境使用token登出
     *
     * @param token
     * @author dongxiayu
     * @date 2021/10/15 13:12
     */
    @Override
    public CommonResult<String> doLogout(String token) {
        return null;
    }
}
