package vip.xiaonuo.sys.core.enums;

import lombok.Getter;

/**
 * Oauth用户性别枚举
 *
 * @author xuyuxiang
 * @date 2020/7/29 10:32
 **/
@Getter
public enum OauthSexEnum {

    /**
     * 男
     */
    MAN("1", "男"),

    /**
     * 女
     */
    WOMAN("0", "女"),

    /**
     * 未知
     */
    NONE("-1", "未知");

    private final String code;

    private final String message;

    OauthSexEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
