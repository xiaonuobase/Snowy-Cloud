package vip.xiaonuo.common.context.login;

import cn.hutool.extra.spring.SpringUtil;

/**
 * 当前登录用户信息获取的接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 12:21
 */
public class LoginContextHolder {

    public static LoginContext me() {
        return SpringUtil.getBean(LoginContext.class);
    }

}
