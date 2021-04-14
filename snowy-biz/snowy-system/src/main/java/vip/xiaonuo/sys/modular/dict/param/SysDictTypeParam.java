package vip.xiaonuo.sys.modular.dict.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统字典类型参数
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictTypeParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class, changeStatus.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {add.class, edit.class, dropDown.class,})
    private String code;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空，请检查sort参数", groups = {add.class, edit.class})
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = {changeStatus.class})
    private Integer status;
}
