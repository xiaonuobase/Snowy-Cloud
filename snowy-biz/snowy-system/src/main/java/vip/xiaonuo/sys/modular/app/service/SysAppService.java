package vip.xiaonuo.sys.modular.app.service;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.app.entity.SysApp;
import vip.xiaonuo.sys.modular.app.param.SysAppParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统应用service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 16:14
 */
public interface SysAppService extends IService<SysApp> {

    /**
     * 获取用户应用相关信息
     *
     * @param userId 用户id
     * @return 用户拥有的应用信息，格式：[{"code":"system","name":"系统应用","active":true}]
     * @author xuyuxiang
     * @date 2020/3/13 16:25
     */
    List<Dict> getLoginApps(Long userId);

    /**
     * 查询系统应用
     *
     * @param sysAppParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/24 20:55
     */
    PageResult<SysApp> page(SysAppParam sysAppParam);

    /**
     * 添加系统应用
     *
     * @param sysAppParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void add(SysAppParam sysAppParam);

    /**
     * 删除系统应用
     *
     * @param sysAppParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void delete(SysAppParam sysAppParam);

    /**
     * 编辑系统应用
     *
     * @param sysAppParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/25 14:58
     */
    void edit(SysAppParam sysAppParam);

    /**
     * 查看系统应用
     *
     * @param sysAppParam 查看参数
     * @return 系统应用
     * @author xuyuxiang
     * @date 2020/3/26 9:50
     */
    SysApp detail(SysAppParam sysAppParam);

    /**
     * 系统应用列表
     *
     * @param sysAppParam 查询参数
     * @return 系统应用列表
     * @author xuyuxiang
     * @date 2020/4/19 14:56
     */
    List<SysApp> list(SysAppParam sysAppParam);

    /**
     * 设为默认应用
     *
     * @param sysAppParam 设为默认应用参数
     * @author xuyuxiang
     * @date 2020/6/29 16:49
     */
    void setAsDefault(SysAppParam sysAppParam);
}
