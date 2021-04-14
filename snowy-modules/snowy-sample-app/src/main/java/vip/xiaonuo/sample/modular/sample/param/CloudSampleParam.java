package vip.xiaonuo.sample.modular.sample.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 微服务范例参数
 *
 * @author dongxiayu
 * @date 2020/12/28 00:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CloudSampleParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数",
            groups = {edit.class, delete.class, detail.class})
    private Long id;

    /**
     * 范例名称
     */
    @NotBlank(message = "范例名称不能为空，请检查sampleName参数", groups = {add.class, edit.class})
    private String sampleName;

}
