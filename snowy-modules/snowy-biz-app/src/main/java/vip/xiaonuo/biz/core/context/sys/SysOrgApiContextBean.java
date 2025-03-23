/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.biz.core.context.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONArray;
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
import java.util.Objects;

/**
 * 机构API上下文Bean
 *
 * @author yubaoshan
 * @date 2025/3/24 02:39
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysOrgApiContextBean implements SysOrgApi {

    private final SysOrgFeign sysOrgFeign;

    /**
     * 根据id获取名称
     *
     * @param orgId
     * @author dongxiayu
     * @date 2022/8/4 10:12
     */
    @Override
    public String getNameById(String orgId) {
        return sysOrgFeign.getNameById(orgId);
    }

    /**
     * 根据组织id获取部门主管id
     *
     * @param orgId
     * @author dongxiayu
     * @date 2022/6/6 14:50
     */
    @Override
    public String getSupervisorIdByOrgId(String orgId) {
        return sysOrgFeign.getSupervisorIdByOrgId(orgId);
    }

    /**
     * 获取组织树选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:46
     **/
    @Override
    public List<Tree<String>> orgTreeSelector() {
        String feignResp = sysOrgFeign.orgTreeSelector();
        JSONArray jsonArray = new JSONArray(feignResp);

        Iterable<JSONObject> jsonObjectIterable = jsonArray.jsonIter();

        List<Tree<String>> treeList = null;
        while (jsonObjectIterable.iterator().hasNext()){
            if(Objects.isNull(treeList)){
                treeList = CollUtil.newArrayList();
            }

            JSONObject jsonObject = jsonObjectIterable.iterator().next();
            TypeReference typeReference = new TypeReference<Tree<String>>() {};
            Tree<String> treeObj = (Tree<String>) jsonObject.toBean(typeReference);
            treeList.add(treeObj);
        }

        return treeList;
    }

    /**
     * 获取组织列表选择器
     *
     * @param parentId
     * @author dongxiayu
     * @date 2022/7/22 14:45
     */
    @Override
    public Page<JSONObject> orgListSelector(String parentId) {
        long current = CommonPageRequest.defaultPage().getCurrent();
        long size = CommonPageRequest.defaultPage().getSize();
        String feignResp = sysOrgFeign.orgListSelector(Convert.toInt(current), Convert.toInt(size), parentId);
        Page<JSONObject> resp = (Page<JSONObject>)JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }
}
