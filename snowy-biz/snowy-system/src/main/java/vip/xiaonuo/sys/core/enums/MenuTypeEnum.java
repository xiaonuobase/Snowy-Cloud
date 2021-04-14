package vip.xiaonuo.sys.core.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author xuyuxiang
 * @date 2020/5/25 15:18
 */
@Getter
public enum MenuTypeEnum {

    /**
     * 目录
     */
    DIR(0, "目录"),

    /**
     * 菜单
     */
    MENU(1, "菜单"),

    /**
     * 按钮
     */
    BTN(2, "按钮");

    private final Integer code;

    private final String message;

    MenuTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
