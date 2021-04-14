package vip.xiaonuo.sample.core.consts;

/**
 * 系统管理异常枚举编码构成常量
 * <p>
 * 异常枚举编码由3部分组成，如下：
 * <p>
 * 模块编码（2位） + 分类编码（4位） + 具体项编码（至少1位）
 * <p>
 * 模块编码和分类编码在 ExampleExpEnumConstant 类中声明
 *
 * @author yubaoshan
 * @date 2020/6/19 20:46
 */
public interface ExampleExpEnumConstant {

    /**
     * 模块分类编码（2位）
     * <p>
     * snowy-example-app 模块异常枚举编码
     */
    int SNOWY_SYS_MODULE_EXP_CODE = 21;


}
