package vip.xiaonuo.generate.modular.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.generate.modular.entity.CodeGenerate;
import vip.xiaonuo.generate.modular.param.CodeGenerateParam;
import vip.xiaonuo.generate.modular.result.InformationResult;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 代码生成基础配置service接口
 *
 * @author yubaoshan
 * @date 2020年12月16日21:03:15
 */
public interface CodeGenerateService extends IService<CodeGenerate> {

    /**
     * 查询代码生成基础配置
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    PageResult<CodeGenerate> page(CodeGenerateParam codeGenerateParam);

    /**
     * 添加查询代码生成基础配置
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    void add(CodeGenerateParam codeGenerateParam);

    /**
     * 删除查询代码生成基础配置
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    void delete(List<CodeGenerateParam> codeGenerateParamList);

    /**
     * 编辑查询代码生成基础配置
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    void edit(CodeGenerateParam codeGenerateParam);

    /**
     * 查看查询代码生成基础配置
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    CodeGenerate detail(CodeGenerateParam codeGenerateParam);

    /**
     * 查询当前数据库用户下的所有表
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    List<InformationResult> InformationTableList ();

    /**
     * 本地生成代码
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    void runLocal(CodeGenerateParam codeGenerateParam);

    /**
     * 下载zip方式
     *
     * @author yubaoshan
     * @date 2020年12月16日21:03:15
     */
    void runDown(CodeGenerateParam codeGenerateParam, HttpServletResponse response);
}
