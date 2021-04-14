package vip.xiaonuo.api.auth.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import vip.xiaonuo.common.validation.date.DateValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * 系统用户参数
 *
 * @author xuyuxiang
 * @date 2020/3/23 9:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数",
            groups = {edit.class, delete.class, detail.class, start.class,
                    stop.class, grantRole.class, grantData.class, updateInfo.class,
                    updatePwd.class, resetPwd.class, changeStatus.class, updateAvatar.class})
    private Long id;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空，请检查account参数", groups = {add.class, edit.class})
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空，请检查password参数", groups = {updatePwd.class})
    private String password;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空，请检查newPassword参数", groups = {updatePwd.class})
    private String newPassword;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;

    /**
     * 头像
     */
    @NotNull(message = "头像不能为空，请检查avatar参数", groups = {updateAvatar.class})
    private Long avatar;

    /**
     * 生日
     */
    @DateValue(message = "生日格式不正确，请检查birthday参数", groups = {add.class, edit.class, updateInfo.class})
    private String birthday;

    /**
     * 性别(字典 1男 2女)
     */
    @NotNull(message = "性别不能为空，请检查sex参数", groups = {updateInfo.class})
    @Min(value = 1, message = "性别格式错误，请检查sex参数", groups = {updateInfo.class})
    @Max(value = 2, message = "性别格式错误，请检查sex参数", groups = {updateInfo.class})
    private Integer sex;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误，请检查email参数", groups = {updateInfo.class})
    private String email;

    /**
     * 手机
     */
    @NotNull(message = "手机号码不能为空，请检查phone参数", groups = {add.class, edit.class, updateInfo.class})
    @Size(min = 11, max = 11, message = "手机号码格式错误，请检查phone参数", groups = {add.class, edit.class, updateInfo.class})
    private String phone;

    /**
     * 电话
     */
    private String tel;

    /**
     * 授权角色
     */
    @NotNull(message = "授权角色不能为空，请检查grantRoleIdList参数", groups = {grantRole.class})
    private List<Long> grantRoleIdList;

    /**
     * 授权数据
     */
    @NotNull(message = "授权数据不能为空，请检查grantOrgIdList参数", groups = {grantData.class})
    private List<Long> grantOrgIdList;

    /*==============员工相关信息==========*/

    @NotNull(message = "员工信息不能为空，请检查sysEmpParam参数", groups = {add.class, edit.class})
    @Valid
    private SysEmpParam sysEmpParam;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = changeStatus.class)
    private Integer status;
}
