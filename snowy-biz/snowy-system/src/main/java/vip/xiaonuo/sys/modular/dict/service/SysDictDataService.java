package vip.xiaonuo.sys.modular.dict.service;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.dict.entity.SysDictData;
import vip.xiaonuo.sys.modular.dict.param.SysDictDataParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统字典值service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 16:10
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 查询系统字典值
     *
     * @param sysDictDataParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/31 20:53
     */
    PageResult<SysDictData> page(SysDictDataParam sysDictDataParam);

    /**
     * 系统字典值列表
     *
     * @param sysDictDataParam 查询参数
     * @return 系统字典值列表
     * @author xuyuxiang
     * @date 2020/3/31 21:07
     */
    List<SysDictData> list(SysDictDataParam sysDictDataParam);

    /**
     * 添加系统字典值
     *
     * @param sysDictDataParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/31 20:53
     */
    void add(SysDictDataParam sysDictDataParam);

    /**
     * 删除系统字典值
     *
     * @param sysDictDataParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/31 20:54
     */
    void delete(SysDictDataParam sysDictDataParam);

    /**
     * 编辑系统字典值
     *
     * @param sysDictDataParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/31 20:54
     */
    void edit(SysDictDataParam sysDictDataParam);

    /**
     * 查看系统字典值
     *
     * @param sysDictDataParam 查看参数
     * @return 系统字典值
     * @author xuyuxiang
     * @date 2020/3/31 20:54
     */
    SysDictData detail(SysDictDataParam sysDictDataParam);

    /**
     * 根据typeId下拉
     *
     * @param dictTypeId 字典类型id
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     * @author xuyuxiang yubaoshan
     * @date 2020/3/31 21:27
     */
    List<Dict> getDictDataListByDictTypeId(Long dictTypeId);

    /**
     * 根据typeId删除
     *
     * @param dictTypeId 字典类型id
     * @author xuyuxiang
     * @date 2020/4/1 10:27
     */
    void deleteByTypeId(Long dictTypeId);

    /**
     * 修改状态
     *
     * @param sysDictDataParam 修改参数
     * @author yubaoshan
     * @date 2020/5/1 9:44
     */
    void changeStatus(SysDictDataParam sysDictDataParam);

    /**
     * 根据字典类型获取字典的code值
     *
     * @param dictTypeCodes 字典类型编码集合
     * @return 字典编码值列表
     * @author xuyuxiang
     * @date 2020/8/9 14:18
     */
    List<String> getDictCodesByDictTypeCode(String... dictTypeCodes);
}
