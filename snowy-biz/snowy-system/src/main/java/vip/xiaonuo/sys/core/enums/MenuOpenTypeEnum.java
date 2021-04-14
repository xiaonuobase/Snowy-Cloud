package vip.xiaonuo.sys.core.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author xuyuxiang
 * @date 2020/5/25 15:18
 */
@Getter
public enum MenuOpenTypeEnum {

    /**
     * 无
     */
    NONE(0, "无"),

    /**
     * 组件
     */
    COMPONENT(1, "组件"),

    /**
     * 内链
     */
    INNER(2, "内链"),

    /**
     * 外链
     */
    OUTER(3, "外链");

    private final Integer code;

    private final String message;

    MenuOpenTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
