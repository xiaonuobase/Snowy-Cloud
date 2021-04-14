package vip.xiaonuo.sys.modular.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统角色数据范围表
 *
 * @author xuyuxiang
 * @date 2020/3/11 11:47
 */
@Data
@TableName("sys_role_data_scope")
public class SysRoleDataScope {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 机构id
     */
    private Long orgId;
}
