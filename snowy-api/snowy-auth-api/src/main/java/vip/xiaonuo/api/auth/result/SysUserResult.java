package vip.xiaonuo.api.auth.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统用户结果
 *
 * @author xuyuxiang
 * @date 2020/4/2 9:19
 */
@Data
public class SysUserResult {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private Long avatar;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别(字典 1男 2女 3未知)
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 电话
     */
    private String tel;

    /**
     * 用户员工信息
     */
    private SysEmpInfo sysEmpInfo;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;
}
