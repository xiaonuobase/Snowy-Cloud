package vip.xiaonuo.auth.feign.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.auth.api.AuthInnerApi;
import vip.xiaonuo.auth.feign.AuthInnerFeign;
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

    private final AuthInnerApi authInnerApi;

    /**
     * B端账号密码内部环境登录
     *
     * @param username
     * @param password
     * @author dongxiayu
     * @date 2021/10/15 13:12
     */
    @Override
    @PostMapping("/feign/auth/b/inner/doLogin")
    public CommonResult<String> doLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        return authInnerApi.doLogin(username, password);
    }

    /**
     * B端账号密码内部环境登出
     *
     * @param token
     * @author dongxiayu
     * @date 2021/10/15 13:12
     */
    @Override
    @PostMapping("/feign/auth/b/inner/doLogout")
    public CommonResult<String> doLogout(@RequestParam("token") String token) {
        return authInnerApi.doLogout(token);
    }
}