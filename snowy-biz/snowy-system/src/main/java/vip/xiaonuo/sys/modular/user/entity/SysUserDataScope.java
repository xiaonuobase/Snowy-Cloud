package vip.xiaonuo.sys.modular.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统用户数据范围表
 *
 * @author xuyuxiang
 * @date 2020/3/11 11:47
 */
@Data
@TableName("sys_user_data_scope")
public class SysUserDataScope {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 机构id
     */
    private Long orgId;
}
