package vip.xiaonuo.sys.api.context;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
     * 获取组织树选择器（懒加载）
     *
     * @author xuyuxiang
     * @date 2026/3/8 14:46
     **/
    @Override
    public List<JSONObject> orgTreeSelector(String parentId, String searchKey) {
        return this.sysOrgFeign.orgTreeSelector(parentId, searchKey);
    }

    /**
     * 获取组织树选择器
     *
     * @author xuyuxiang
     * @date 2022/7/22 14:46
     **/
    @Override
    public Page<JSONObject> orgListSelector(String parentId, String searchKey) {
        String feignResp = this.sysOrgFeign.orgListSelector(parentId, searchKey);
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

    /**
     * 清空缓存
     *
     * @author yubaoshan
     * @date 2026/3/8 14:50
     **/
    @Override
    public void clearOrgCache() {
        this.sysOrgFeign.clearOrgCache();
    }

    /**
     * 根据组织id获取其及所有下级组织id列表
     *
     * @param orgId
     * @author yubaoshan
     * @date 2026/4/5 14:25
     */
    @Override
    public List<String> getChildOrgIdListById(String orgId) {
        return this.sysOrgFeign.getChildOrgIdListById(orgId);
    }
}
