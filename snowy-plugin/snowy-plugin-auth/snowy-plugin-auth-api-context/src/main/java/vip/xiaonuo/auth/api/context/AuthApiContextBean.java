package vip.xiaonuo.auth.api.context;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.auth.api.AuthApi;
import vip.xiaonuo.auth.feign.AuthFeign;

import java.util.List;

/**
 *
 * AuthApi上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthApiContextBean implements AuthApi {

    private final AuthFeign authFeign;

    /**
     * 获取基础登录业务数据，b端在线用户，c端在线用户
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public JSONObject getUserSessionCount() {
        return this.authFeign.getUserSessionCount();
    }

    /**
     * 获取三方用户总量
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public Long getThirdUserCount() {
        return this.authFeign.getThirdUserCount();
    }

    /**
     * 获取B端验证码是否开启
     *
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     **/
    @Override
    public boolean getDefaultCaptchaOpenForB() {
        return this.authFeign.getDefaultCaptchaOpenForB();
    }

    /**
     * 获取C端验证码是否开启
     *
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     **/
    @Override
    public boolean getDefaultCaptchaOpenForC() {
        return this.authFeign.getDefaultCaptchaOpenForC();
    }

    /**
     * 校验验证码
     *
     * @param phoneOrEmail
     * @param validCode
     * @param validCodeReqNo
     * @author xuyuxiang
     * @date 2021/12/28 14:46
     */
    @Override
    public void validValidCode(String phoneOrEmail, String validCode, String validCodeReqNo) {
        this.authFeign.validValidCode(phoneOrEmail, validCode, validCodeReqNo);
    }

    /**
     * B端账号密码登录
     *
     * @param account
     * @param password
     * @param validCode
     * @param validCodeReqNo
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginForB(String account, String password, String validCode, String validCodeReqNo) {
        return this.authFeign.doLoginForB(account, password, validCode, validCodeReqNo);
    }

    /**
     * C端账号密码登录
     *
     * @param account
     * @param password
     * @param validCode
     * @param validCodeReqNo
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginForC(String account, String password, String validCode, String validCodeReqNo) {
        return this.authFeign.doLoginForC(account, password, validCode, validCodeReqNo);
    }

    /**
     * B端用户id登录
     *
     * @param userId
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByIdForB(String userId, String device) {
        return this.authFeign.doLoginByIdForB(userId, device);
    }

    /**
     * C端用户id登录
     *
     * @param userId
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByIdForC(String userId, String device) {
        return this.authFeign.doLoginByIdForC(userId, device);
    }

    /**
     * B端账号登录
     *
     * @param account
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByAccountForB(String account, String device) {
        return this.authFeign.doLoginByAccountForB(account, device);
    }

    /**
     * C端账号登录
     *
     * @param account
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByAccountForC(String account, String device) {
        return this.authFeign.doLoginByAccountForC(account, device);
    }

    /**
     * B端手机号登录
     *
     * @param phone
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByPhoneForB(String phone, String device) {
        return this.authFeign.doLoginByPhoneForB(phone, device);
    }

    /**
     * C端手机号登录
     *
     * @param phone
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByPhoneForC(String phone, String device) {
        return this.authFeign.doLoginByPhoneForC(phone, device);
    }

    /**
     * B端邮箱登录
     *
     * @param email
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByEmailForB(String email, String device) {
        return this.authFeign.doLoginByEmailForB(email, device);
    }

    /**
     * C端邮箱登录
     *
     * @param email
     * @param device
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @Override
    public String doLoginByEmailForC(String email, String device) {
        return this.authFeign.doLoginByEmailForC(email, device);
    }

    /**
     * 根据用户id删除三方用户
     *
     * @author xuyuxiang
     * @date 2022/7/9 14:58
     */
    @Override
    public void removeThirdUserByUserIdList(List<String> userIdList) {
        this.authFeign.removeThirdUserByUserIdList(userIdList);
    }
}
