package vip.xiaonuo.sys.modular.oauth.service;

import vip.xiaonuo.sys.modular.oauth.entity.SysOauthUser;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthCallback;

import javax.servlet.http.HttpServletRequest;

/**
 * Oauth登录相关service接口
 *
 * @author xuyuxiang
 * @date 2020/7/28 17:06
 **/
public interface SysOauthService extends IService<SysOauthUser> {

    /**
     * 根据授权平台来源获取授权地址
     *
     * @param source 授权平台来源
     * @return 授权地址
     * @author xuyuxiang
     * @date 2020/7/28 17:26
     **/
    String getAuthorizeUrl(String source);

    /**
     * 授权后回调方法
     *
     * @param source   授权来源平台
     * @param callback 授权平台返回的用户信息
     * @param request  request请求
     * @return 登录成功的token
     * @author xuyuxiang
     * @date 2020/7/29 9:48
     **/
    String callback(String source, AuthCallback callback, HttpServletRequest request);
}
