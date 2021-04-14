package vip.xiaonuo.api.auth.param;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 员工参数
 *
 * @author xuyuxiang
 * @date 2020/4/1 19:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysEmpParam extends BaseParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 工号
     */
    private String jobNum;

    /**
     * 所属机构id
     */
    @NotNull(message = "所属机构id不能为空，请检查sysEmpParam.orgId参数", groups = {add.class, edit.class})
    private Long orgId;

    /**
     * 所属机构名称
     */
    @NotBlank(message = "所属机构名称不能为空，请检查sysEmpParam.orgName参数", groups = {add.class, edit.class})
    private String orgName;

    /**
     * 附属机构id和附属职位id集合
     */
    @NotNull(message = "附属信息不能为空，请检查sysEmpParam.extIds参数", groups = {add.class, edit.class})
    private List<Dict> extIds;

    /**
     * 职位id集合
     */
    @NotNull(message = "所属职位不能为空，请检查sysEmpParam.posIdList参数", groups = {add.class, edit.class})
    private List<Long> posIdList;
}
