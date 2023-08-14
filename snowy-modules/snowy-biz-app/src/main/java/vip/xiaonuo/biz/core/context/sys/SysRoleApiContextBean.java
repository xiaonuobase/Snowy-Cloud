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

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.sys.api.SysRoleApi;
import vip.xiaonuo.sys.feign.SysRoleFeign;

import java.util.List;

/**
 * 角色API上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 10:55
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysRoleApiContextBean implements SysRoleApi {

    private final SysRoleFeign sysRoleFeign;

    /**
     * 判断组织下是否存在角色
     *
     * @param orgIdList
     * @author dongxiayu
     * @date 2022/8/2 11:16
     */
    @Override
    public boolean orgHasRole(List<String> orgIdList) {
        return sysRoleFeign.orgHasRole(orgIdList);
    }

    /**
     * 获取角色选择器
     *
     * @param orgId
     * @param category
     * @param searchKey
     * @author dongxiayu
     * @date 2022/7/22 14:49
     */
    @SuppressWarnings("ALL")
    @Override
    public Page<JSONObject> roleSelector(String orgId, String category, String searchKey, List<String> dataScopeList, boolean excludeSuperAdmin) {
        String feignResp = sysRoleFeign.roleSelector(orgId, category, searchKey, dataScopeList, excludeSuperAdmin);
        Page<JSONObject> resp =  (Page<JSONObject>)JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }

    /**
     * 代码生成菜单按钮授权
     *
     * @param menuId
     * @author dongxiayu
     * @date 2022/11/1 15:58
     */
    @Override
    public void grantForGenMenuAndButton(String menuId) {
        sysRoleFeign.grantForGenMenuAndButton(menuId);
    }
}