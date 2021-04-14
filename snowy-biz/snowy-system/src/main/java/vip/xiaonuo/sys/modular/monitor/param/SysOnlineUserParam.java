package vip.xiaonuo.sys.modular.monitor.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 系统在线用户参数
 *
 * @author xuyuxiang
 * @date 2020/4/7 17:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysOnlineUserParam extends BaseParam {

    /**
     * 会话id
     */
    @NotEmpty(message = "sessionId不能为空，请检查sessionId参数", groups = {force.class})
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
