package vip.xiaonuo.sys.modular.consts.entity;

import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参数配置
 * </p>
 *
 * @author yubaoshan
 * @date 2019/6/20 13:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_config")
public class SysConfig extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 属性值
     */
    private String value;

    /**
     * 是否是系统参数（Y-是，N-否）
     */
    private String sysFlag;

    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;

    /**
     * 常量所属分类的编码，来自于“常量的分类”字典
     */
    private String groupCode;

}
