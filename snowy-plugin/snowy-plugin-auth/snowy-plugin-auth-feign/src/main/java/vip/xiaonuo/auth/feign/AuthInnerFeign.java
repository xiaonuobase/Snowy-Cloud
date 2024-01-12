package vip.xiaonuo.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.CommonResult;

/**
 * AuthFeign
 *
 * @author dongxiayu
 * @date 2024/1/12 23:13
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "AuthFeign")
public interface AuthInnerFeign {

    /**
     * B端账号密码内部环境登录
     *
     * @author dongxiayu
     * @date 2021/10/15 13:12
     **/
    @PostMapping("/auth/b/inner/doLogin")
    CommonResult<String> doLogin(@RequestParam("username") String username,@RequestParam("password") String password);

    /**
     * B端账号密码内部环境登出
     *
     * @author dongxiayu
     * @date 2021/10/15 13:12
     **/
    @PostMapping("/auth/b/inner/doLogout")
    CommonResult<String> doLogout(@RequestParam("token") String token);

}
