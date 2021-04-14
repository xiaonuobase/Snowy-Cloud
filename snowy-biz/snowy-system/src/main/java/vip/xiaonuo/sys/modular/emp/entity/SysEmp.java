package vip.xiaonuo.sys.modular.emp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 员工表
 *
 * @author xuyuxiang
 * @date 2020/3/11 11:20
 */
@Data
@TableName("sys_emp")
public class SysEmp {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工号
     */
    private String jobNum;

    /**
     * 所属机构id
     */
    private Long orgId;

    /**
     * 所属机构名称
     */
    private String orgName;
}
