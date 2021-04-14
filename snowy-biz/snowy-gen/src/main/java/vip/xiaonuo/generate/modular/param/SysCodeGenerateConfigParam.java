package vip.xiaonuo.generate.modular.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
* 代码生成详细配置参数类
 *
 * @author yubaoshan
 * @date 2021-02-06 20:19:49
*/
@Data
public class SysCodeGenerateConfigParam extends BaseParam {

    /**
     * 主键
     */
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
     * 列表展示
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
     * 主外键名称
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

    /**
     * 代码生成配置列表
     */
    @NotEmpty(message = "代码生成配置列表，请检查sysCodeGenerateConfigParamList参数", groups = {edit.class})
    private List<SysCodeGenerateConfigParam> sysCodeGenerateConfigParamList;

}
