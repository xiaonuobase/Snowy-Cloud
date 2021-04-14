package vip.xiaonuo.common.pojo.oauth;

import lombok.Data;

/**
 * Oauth第三方登录配置
 *
 * @author xuyuxiang
 * @date 2020/7/28 17:18
 **/
@Data
public class OauthConfigs {

    /**
     * clientId
     */
    private String clientId;

    /**
     * clientSecret
     */
    private String clientSecret;

    /**
     * 回调地址
     */
    private String redirectUri;
}
