package vip.xiaonuo.sys.modular.dict.service;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.dict.entity.SysDictType;
import vip.xiaonuo.sys.modular.dict.param.SysDictTypeParam;
import vip.xiaonuo.sys.modular.dict.result.SysDictTreeNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统字典类型service接口
 *
 * @author xuyuxiang，xuyuxiang
 * @date 2020/3/13 16:10
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 查询系统字典类型
     *
     * @param sysDictTypeParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:34
     */
    PageResult<SysDictType> page(SysDictTypeParam sysDictTypeParam);

    /**
     * 获取字典类型列表
     *
     * @param sysDictTypeParam 查询参数
     * @return 系统字典类型列表
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 21:07
     */
    List<SysDictType> list(SysDictTypeParam sysDictTypeParam);

    /**
     * 系统字典类型下拉
     *
     * @param sysDictTypeParam 下拉参数
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 21:23
     */
    List<Dict> dropDown(SysDictTypeParam sysDictTypeParam);

    /**
     * 添加系统字典类型
     *
     * @param sysDictTypeParam 添加参数
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:35
     */
    void add(SysDictTypeParam sysDictTypeParam);

    /**
     * 删除系统字典类型
     *
     * @param sysDictTypeParam 删除参数
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:35
     */
    void delete(SysDictTypeParam sysDictTypeParam);

    /**
     * 编辑系统字典类型
     *
     * @param sysDictTypeParam 编辑参数
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:35
     */
    void edit(SysDictTypeParam sysDictTypeParam);

    /**
     * 查看系统字典类型
     *
     * @param sysDictTypeParam 查看参数
     * @return 系统字典类型
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:35
     */
    SysDictType detail(SysDictTypeParam sysDictTypeParam);

    /**
     * 修改状态（字典 0正常 1停用 2删除）
     *
     * @param sysDictTypeParam 修改参数
     * @author yubaoshan
     * @date 2020/4/30 22:30
     */
    void changeStatus(SysDictTypeParam sysDictTypeParam);

    /**
     * 系统字典类型与字典值构造的树
     *
     * @return 树
     * @author xuyuxiang
     * @date 2020/4/30 22:30
     */
    List<SysDictTreeNode> tree();
}
