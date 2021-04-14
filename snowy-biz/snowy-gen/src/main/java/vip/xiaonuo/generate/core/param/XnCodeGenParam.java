package vip.xiaonuo.generate.core.param;

import vip.xiaonuo.generate.core.config.Config;
import vip.xiaonuo.generate.modular.entity.SysCodeGenerateConfig;
import lombok.Data;
import java.util.List;

@Data
public class XnCodeGenParam {

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 类名
     */
    private String className;

    /**
     * 功能名
     */
    private String functionName;

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
     * 数据库表名（经过组装的）
     */
    private String tableNameAss;

    /**
     * 代码包名
     */
    private String packageName;

    /**
     * 生成时间（String类型的）
     */
    private String createTimeString;

    /**
     * 数据库表中字段集合
     */
    private List<SysCodeGenerateConfig> configList;

    /**
     * 模块名
     */
    private String modularNane = Config.MODULAR_NAME;

    /**
     * 业务名
     */
    private String busName;

}
