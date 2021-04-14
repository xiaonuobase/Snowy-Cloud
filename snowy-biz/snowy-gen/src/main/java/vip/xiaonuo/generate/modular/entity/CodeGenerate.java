package vip.xiaonuo.generate.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成基础配置
 *
 * @author yubaoshan
 * @date 2020年12月16日21:04:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_code_generate")
public class CodeGenerate extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 类名
     */
    private String className;

    /**
     * 是否移除表前缀
     */
    private String tablePrefix;

    /**
     * 生成方式
     */
    private String generateType;

    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 包名
     */
    private String packageName;

    /**
     * 业务名（业务代码包名称）
     */
    private String busName;

    /**
     * 功能名（数据库表名称）
     */
    private String tableComment;
}
