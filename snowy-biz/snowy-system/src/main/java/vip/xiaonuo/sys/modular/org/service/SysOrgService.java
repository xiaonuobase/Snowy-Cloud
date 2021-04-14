package vip.xiaonuo.sys.modular.org.service;

import vip.xiaonuo.common.pojo.node.AntdBaseTreeNode;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.org.entity.SysOrg;
import vip.xiaonuo.sys.modular.org.param.SysOrgParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统组织机构service接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 16:02
 */
public interface SysOrgService extends IService<SysOrg> {

    /**
     * 查询系统机构
     *
     * @param sysOrgParam 查询参数
     * @return 查询分页结果
     * @author yubaoshan
     * @date 2020/5/11 15:49
     */
    PageResult<SysOrg> page(SysOrgParam sysOrgParam);

    /**
     * 系统组织机构列表
     *
     * @param sysOrgParam 查询参数
     * @return 组织机构列表
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    List<SysOrg> list(SysOrgParam sysOrgParam);

    /**
     * 添加系统组织机构
     *
     * @param sysOrgParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void add(SysOrgParam sysOrgParam);

    /**
     * 删除系统组织机构
     *
     * @param sysOrgParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void delete(SysOrgParam sysOrgParam);

    /**
     * 编辑系统组织机构
     *
     * @param sysOrgParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/25 14:58
     */
    void edit(SysOrgParam sysOrgParam);

    /**
     * 查看系统组织机构
     *
     * @param sysOrgParam 查看参数
     * @return 组织机构
     * @author xuyuxiang
     * @date 2020/3/26 9:50
     */
    SysOrg detail(SysOrgParam sysOrgParam);

    /**
     * 获取系统组织机构树
     *
     * @param sysOrgParam 查询参数
     * @return 系统组织机构树
     * @author xuyuxiang yubaoshan
     * @date 2020/3/26 11:56
     */
    List<AntdBaseTreeNode> tree(SysOrgParam sysOrgParam);

    /**
     * 根据数据范围类型获取当前登录用户的数据范围id集合
     *
     * @param dataScopeType 数据范围类型（1全部数据 2本部门及以下数据 3本部门数据 4仅本人数据）
     * @param orgId         组织机构id
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/4/5 18:29
     */
    List<Long> getDataScopeListByDataScopeType(Integer dataScopeType, Long orgId);
}
