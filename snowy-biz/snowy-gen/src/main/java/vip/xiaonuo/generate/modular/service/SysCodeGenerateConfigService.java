package vip.xiaonuo.generate.modular.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.generate.modular.entity.CodeGenerate;
import vip.xiaonuo.generate.modular.entity.SysCodeGenerateConfig;
import vip.xiaonuo.generate.modular.param.SysCodeGenerateConfigParam;
import vip.xiaonuo.generate.modular.result.InforMationColumnsResult;

import java.util.List;

/**
 * 代码生成详细配置service接口
 *
 * @author yubaoshan
 * @date 2021-02-06 20:19:49
 */
public interface SysCodeGenerateConfigService extends IService<SysCodeGenerateConfig> {

    /**
     * 代码生成详细配置列表
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
    List<SysCodeGenerateConfig> list(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 添加代码生成详细配置
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
    void add(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 添加代码生成详细配置列表
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
    void addList(List<InforMationColumnsResult> inforMationColumnsResultList, CodeGenerate codeGenerate);

    /**
     * 删除代码生成详细配置
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
    void delete(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 编辑代码生成详细配置
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
    void edit(List<SysCodeGenerateConfigParam> sysCodeGenerateConfigParamList);

    /**
     * 查看代码生成详细配置
     *
     * @author yubaoshan
     * @date 2021-02-06 20:19:49
     */
     SysCodeGenerateConfig detail(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);
}
