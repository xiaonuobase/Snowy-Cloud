package vip.xiaonuo.sys.modular.dict.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统字典值参数
 *
 * @author xuyuxiang
 * @date 2020/3/31 20:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class, changeStatus.class})
    private Long id;

    /**
     * 字典类型id
     */
    @NotNull(message = "字典类型typeId不能为空，请检查typeId参数", groups = {list.class, add.class, edit.class})
    private Long typeId;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空，请检查value参数", groups = {add.class, edit.class})
    private String value;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {dropDown.class, add.class, edit.class})
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
     * 状态（字典 0正常 1停用 2删除）
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = changeStatus.class)
    private Integer status;
}
