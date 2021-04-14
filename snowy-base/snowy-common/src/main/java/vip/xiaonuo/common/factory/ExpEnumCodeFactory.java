package vip.xiaonuo.common.factory;

import vip.xiaonuo.common.annotion.ExpEnumType;

/**
 * 异常枚举code值快速创建
 *
 * @author yubaoshan
 * @date 2020/6/19 21:30
 */
public class ExpEnumCodeFactory {

    public static Integer getExpEnumCode(Class<?> clazz, int code) {

        // 默认的异常响应码
        Integer defaultCode = Integer.valueOf("" + 99 + 9999 + 9);

        if (clazz == null) {
            return defaultCode;
        } else {
            ExpEnumType expEnumType = clazz.getAnnotation(ExpEnumType.class);
            if (expEnumType == null) {
                return defaultCode;
            }
            return Integer.valueOf("" + expEnumType.module() + expEnumType.kind() + code);
        }

    }

}
