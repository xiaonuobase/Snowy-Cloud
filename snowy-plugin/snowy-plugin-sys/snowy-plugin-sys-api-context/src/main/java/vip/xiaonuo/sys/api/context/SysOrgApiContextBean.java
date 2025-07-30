package vip.xiaonuo.sys.api.context;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.sys.api.SysOrgApi;
import vip.xiaonuo.sys.feign.SysOrgFeign;

import java.util.List;

/**
 * 系统模块OrgAPI上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysOrgApiContextBean implements SysOrgApi {

    final private SysOrgFeign sysOrgFeign;

    /**
     * 根据id获取名称
     *
     * @param orgId
     * @author xuyuxiang
     * @date 2022/8/4 10:12
     */
    @Override
    public String getNameById(String orgId) {
        return this.sysOrgFeign.getNameById(orgId);
    }

    /**
     * 根据组织id获取部门主管id
     *
     * @param orgId
     * @author xuyuxiang
     * @date 2022/6/6 14:50
     */
    @Override
    public String getSupervisorIdByOrgId(String orgId) {
        return this.sysOrgFeign.getSupervisorIdByOrgId(orgId);
    }

    /**
     * 获取组织树选择器
     *
     * @author xuyuxiang
     * @date 2022/7/22 14:46
     **/
    @Override
    public List<Tree<String>> orgTreeSelector() {
        List<Tree<String>> orgTreeSelectorList = this.sysOrgFeign.orgTreeSelector();
        return orgTreeSelectorList;
    }

    /**
     * 获取组织列表选择器
     *
     * @param parentId
     * @author xuyuxiang
     * @date 2022/7/22 14:45
     */
    @Override
    public Page<JSONObject> orgListSelector(String parentId) {
        long current = CommonPageRequest.defaultPage().getCurrent();
        long size = CommonPageRequest.defaultPage().getSize();
        String feignResp = this.sysOrgFeign.orgListSelector(Convert.toInt(current), Convert.toInt(size), parentId);
        Page<JSONObject> resp = (Page<JSONObject>) JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }

    /**
     * 获取某组织的所有父级id集合
     *
     * @param orgId
     * @author yubaoshan
     * @date 2025/5/10 12:13
     */
    @Override
    public List<String> getParentIdListByOrgId(String orgId) {
        return this.sysOrgFeign.getParentIdListByOrgId(orgId);
    }

    /**
     * 根据组织id获取组织列表
     *
     * @param orgIdList
     * @author wangshuo
     * @date 2025/01/10 14:45
     */
    @Override
    public List<JSONObject> getOrgListByIdListWithoutException(List<String> orgIdList) {
        return this.sysOrgFeign.getOrgListByIdListWithoutException(orgIdList);
    }
}
