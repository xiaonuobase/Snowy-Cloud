package vip.xiaonuo.sys.modular.monitor.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统在线用户结果集
 *
 * @author xuyuxiang
 * @date 2020/4/7 17:07
 */
@Data
public class SysOnlineUserResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 最后登陆IP
     */
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    private String lastLoginTime;

    /**
     * 最后登陆地址
     */
    private String lastLoginAddress;

    /**
     * 最后登陆所用浏览器
     */
    private String lastLoginBrowser;

    /**
     * 最后登陆所用系统
     */
    private String lastLoginOs;
}
