package vip.xiaonuo.sys.modular.role.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.List;

/**
 * 系统角色参数
 *
 * @author xuyuxiang
 * @date 2020/3/26 19:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class, grantMenu.class, grantData.class})
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
     * 排序
     */
    @NotNull(message = "排序不能为空，请检查sort参数", groups = {add.class, edit.class})
    private Integer sort;

    /**
     * 数据范围类型（字典 1全部数据 2本部门及以下数据 3本部门数据 4仅本人数据 5自定义数据）
     */
    @Null(message = "数据范围类型应该为空， 请移除dataScopeType参数", groups = {add.class, edit.class})
    @NotNull(message = "数据范围类型不能为空，请检查dataScopeType参数", groups = {grantData.class})
    @Min(value = 0, message = "数据范围类型格式错误，请检查dataScopeType参数", groups = {grantData.class})
    @Max(value = 5, message = "数据范围类型格式错误，请检查dataScopeType参数", groups = {grantData.class})
    private Integer dataScopeType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 授权菜单
     */
    @NotNull(message = "授权菜单不能为空，请检查grantMenuIdList参数", groups = {grantMenu.class})
    private List<Long> grantMenuIdList;

    /**
     * 授权数据
     */
    @NotNull(message = "授权数据不能为空，请检查grantOrgIdList参数", groups = {grantData.class})
    private List<Long> grantOrgIdList;
}
