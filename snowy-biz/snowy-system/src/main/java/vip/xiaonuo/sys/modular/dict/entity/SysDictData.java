package vip.xiaonuo.sys.modular.dict.entity;

import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典值表
 *
 * @author xuyuxiang
 * @date 2020/3/11 12:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典类型id
     */
    private Long typeId;

    /**
     * 值
     */
    private String value;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;
}
