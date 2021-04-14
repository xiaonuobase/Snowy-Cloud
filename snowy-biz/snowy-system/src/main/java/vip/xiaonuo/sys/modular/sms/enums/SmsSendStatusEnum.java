package vip.xiaonuo.sys.modular.sms.enums;

import lombok.Getter;

/**
 * 短信发送状态枚举
 *
 * @author yubaoshan
 * @date 2018/5/6 12:30
 */
@Getter
public enum SmsSendStatusEnum {

    /**
     * 未发送
     */
    WAITING(0, "未发送"),

    /**
     * 发送成功
     */
    SUCCESS(1, "发送成功"),

    /**
     * 发送失败
     */
    FAILED(2, "发送失败"),

    /**
     * 失效
     */
    INVALID(3, "失效");

    private final Integer code;

    private final String message;

    SmsSendStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
