package vip.xiaonuo.sys.modular.auth.controller;

import cn.hutool.core.lang.Dict;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import vip.xiaonuo.api.auth.service.AuthService;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.common.context.login.LoginContextHolder;
import vip.xiaonuo.common.exception.AuthException;
import vip.xiaonuo.common.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录控制器
 *
 * @author xuyuxiang
 * @date 2020/3/11 12:20
 */
@RestController
public class SysLoginController {

    @Resource
    private AuthService authService;

    @Lazy
    @Resource
    private CaptchaService captchaService;

    /**
     * 获取是否开启租户的标识
     *
     * @author xuyuxiang
     * @date 2020/9/4
     */
    @GetMapping("/getTenantOpen")
    public ResponseData getTenantOpen() {
        return new SuccessResponseData(ConstantContextHolder.getTenantOpenFlag());
    }

    /**
     * 账号密码登录
     *
     * @author xuyuxiang
     * @date 2020/3/11 15:52
     */
    @PostMapping("/login")
    public ResponseData login(@RequestBody Dict dict) {
        String account = dict.getStr("account");
        String password = dict.getStr("password");
        String tenantCode = dict.getStr("tenantCode");

        //检测是否开启验证码
        if (ConstantContextHolder.getCaptchaOpenFlag()) {
            verificationCode(dict.getStr("code"));
        }

        //如果系统开启了多租户开关，则添加租户的临时缓存
        if (ConstantContextHolder.getTenantOpenFlag()) {
            authService.cacheTenantInfo(tenantCode);
        }

        String token = authService.login(account, password);
        return new SuccessResponseData(token);
    }

    /**
     * 退出登录
     *
     * @author xuyuxiang
     * @date 2020/3/16 15:02
     */
    @GetMapping("/logout")
    public void logout() {
        authService.logout();
    }

    /**
     * 获取当前登录用户信息
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:57
     */
    @GetMapping("/getLoginUser")
    public ResponseData getLoginUser() {
        return new SuccessResponseData(LoginContextHolder.me().getSysLoginUserUpToDate());
    }

    /**
     * 获取验证码开关
     *
     * @author Jax
     * @date 2021/1/21 15:27
     */
    @GetMapping("/getCaptchaOpen")
    public ResponseData getCaptchaOpen() {
        return new SuccessResponseData(ConstantContextHolder.getCaptchaOpenFlag());
    }

    /**
     * 校验验证码
     *
     * @author Jax
     * @date 2021/1/21 15:27
     */
    private boolean verificationCode(String code) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(code);
        if (!captchaService.verification(vo).isSuccess()) {
            throw new AuthException(AuthExceptionEnum.CONSTANT_EMPTY_ERROR);
        }
        return true;
    }

}
