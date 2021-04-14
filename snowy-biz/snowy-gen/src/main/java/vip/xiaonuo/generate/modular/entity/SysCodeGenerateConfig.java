package vip.xiaonuo.generate.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成详细配置
 *
 * @author yubaoshan
 * @date 2021-02-06 20:19:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_code_generate_config")
public class SysCodeGenerateConfig extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 代码生成主表ID
     */
    private Long codeGenId;

    /**
     * 数据库字段名
     */
    private String columnName;

    /**
     * java类字段名
     */
    private String javaName;

    /**
     * 字段描述
     */
    private String columnComment;

    /**
     * java类型
     */
    private String javaType;

    /**
     * 作用类型（字典）
     */
    private String effectType;

    /**
     * 字典code
     */
    private String dictTypeCode;

    /**
     * 列表是否缩进（字典）
     */
    private String whetherRetract;

    /**
     * 是否必填（字典）
     */
    private String whetherRequired;

    /**
     * 是否是查询条件
     */
    private String queryWhether;

    /**
     * 查询方式
     */
    private String queryType;

    /**
     * 列表显示
     */
    private String whetherTable;

    /**
     * 增改
     */
    private String whetherAddUpdate;

    /**
     * 主外键
     */
    public String columnKey;

    /**
     * 首字母大写名称（用于代码生成get set方法）
     */
    public String columnKeyName;

    /**
     * 数据库中类型（物理类型）
     */
    public String dataType;

    /**
     * 是否是通用字段
     */
    public String whetherCommon;

}
