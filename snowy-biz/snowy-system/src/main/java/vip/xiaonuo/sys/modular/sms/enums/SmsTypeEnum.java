package vip.xiaonuo.sys.modular.sms.enums;

import lombok.Getter;

/**
 * 短信类型枚举
 *
 * @author yubaoshan
 * @date 2018/5/6 12:30
 */
@Getter
public enum SmsTypeEnum {

    /**
     * 验证类短信
     */
    SMS(1, "验证类短信"),

    /**
     * 纯发送短信
     */
    MESSAGE(2, "纯发送短信");

    private final Integer code;

    private final String message;

    SmsTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
