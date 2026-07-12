package vip.xiaonuo.auth.feign;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * AuthFeign
 *
 * @author dongxiayu
 * @date 2024/1/12 23:13
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "AuthFeign")
public interface AuthFeign {

    /**
     * 获取基础登录业务数据，b端在线用户，c端在线用户
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/getUserSessionCount")
    JSONObject getUserSessionCount();

    /**
     * 获取三方用户总量
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/getThirdUserCount")
    Long getThirdUserCount();

    /**
     * 获取B端验证码是否开启
     *
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     **/
    @PostMapping("/feign/auth/getDefaultCaptchaOpenForB")
    boolean getDefaultCaptchaOpenForB();

    /**
     * 获取C端验证码是否开启
     *
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     **/
    @PostMapping("/feign/auth/getDefaultCaptchaOpenForC")
    boolean getDefaultCaptchaOpenForC();

    /**
     * 校验验证码
     *
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     **/
    @PostMapping("/feign/auth/validValidCode")
    void validValidCode(@RequestParam("phoneOrEmail") String phoneOrEmail,
                        @RequestParam("validCode") String validCode,
                        @RequestParam("validCodeReqNo") String validCodeReqNo);

    /**
     * B端账号密码登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginForB")
    String doLoginForB(@RequestParam("account") String account,
                       @RequestParam("password") String password,
                       @RequestParam("validCode") String validCode,
                       @RequestParam("validCodeReqNo") String validCodeReqNo);

    /**
     * C端账号密码登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginForC")
    String doLoginForC(@RequestParam("account") String account,
                       @RequestParam("password") String password,
                       @RequestParam("validCode") String validCode,
                       @RequestParam("validCodeReqNo") String validCodeReqNo);

    /**
     * B端用户id登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByIdForB")
    String doLoginByIdForB(@RequestParam("userId") String userId,
                           @RequestParam("device") String device);

    /**
     * C端用户id登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByIdForC")
    String doLoginByIdForC(@RequestParam("userId") String userId,
                           @RequestParam("device") String device);

    /**
     * B端账号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByAccountForB")
    String doLoginByAccountForB(@RequestParam("account") String account,
                                @RequestParam("device") String device);

    /**
     * C端账号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByAccountForC")
    String doLoginByAccountForC(@RequestParam("account") String account,
                                @RequestParam("device") String device);

    /**
     * B端手机号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByPhoneForB")
    String doLoginByPhoneForB(@RequestParam("phone") String phone,
                              @RequestParam("device") String device);

    /**
     * C端手机号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByPhoneForC")
    String doLoginByPhoneForC(@RequestParam("phone") String phone,
                              @RequestParam("device") String device);

    /**
     * B端邮箱登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByEmailForB")
    String doLoginByEmailForB(@RequestParam("email") String email,
                              @RequestParam("device") String device);

    /**
     * C端邮箱登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/doLoginByEmailForC")
    String doLoginByEmailForC(@RequestParam("email") String email,
                              @RequestParam("device") String device);

    /**
     * 根据用户id删除三方用户
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @PostMapping("/feign/auth/removeThirdUserByUserIdList")
    void removeThirdUserByUserIdList(@RequestBody List<String> userIdLis);
}
