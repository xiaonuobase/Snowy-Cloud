package vip.xiaonuo.sys.modular.consts.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import vip.xiaonuo.common.validation.flag.FlagValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统参数配置参数
 *
 * @author xuyuxiang
 * @date 2020/4/14 10:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {add.class, edit.class})
    private String code;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空，请检查value参数", groups = {add.class, edit.class})
    private String value;

    /**
     * 是否是系统参数（Y-是，N-否）
     */
    @NotBlank(message = "是否是系统参数不能为空，请检查sysFlag参数", groups = {add.class, edit.class})
    @FlagValue(message = "是否是系统参数格式错误，正确格式应该Y或者N，请检查sysFlag参数", groups = {add.class, edit.class})
    private String sysFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 常量所属分类的编码，来自于“常量的分类”字典
     */
    @NotBlank(message = "值不能为空，请检查value参数", groups = {add.class, edit.class})
    private String groupCode;
}
