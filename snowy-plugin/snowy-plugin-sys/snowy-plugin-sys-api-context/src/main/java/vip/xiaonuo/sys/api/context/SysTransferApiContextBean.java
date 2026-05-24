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
package vip.xiaonuo.sys.api.context;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import vip.xiaonuo.sys.api.SysTransferApi;
import vip.xiaonuo.sys.feign.SysTransferFeign;

import java.util.List;

/**
 * 权限转移Api上下文Bean
 *
 * @author yubaoshan
 * @date 2026/05/17
 **/
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnMissingBean(name = "sysUserTransferServiceImpl")
public class SysTransferApiContextBean implements SysTransferApi {

    private final SysTransferFeign sysTransferFeign;

    @Override
    public List<JSONObject> getTransferResourceList(String sourceUserId) {
        String feignResp = this.sysTransferFeign.getTransferResourceList(sourceUserId);
        return JSONUtil.toList(feignResp, JSONObject.class);
    }

    @Override
    public List<JSONObject> getTransferResourceDetail(String sourceUserId, String resourceType) {
        String feignResp = this.sysTransferFeign.getTransferResourceDetail(sourceUserId, resourceType);
        return JSONUtil.toList(feignResp, JSONObject.class);
    }

    @Override
    public void executeTransfer(JSONObject executeParam) {
        this.sysTransferFeign.executeTransfer(JSONUtil.toJsonStr(executeParam));
    }

    @Override
    public List<JSONObject> getTransferOrgTreeSelector(JSONObject param) {
        String feignResp = this.sysTransferFeign.getTransferOrgTreeSelector(JSONUtil.toJsonStr(param));
        return JSONUtil.toList(feignResp, JSONObject.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<JSONObject> getTransferUserSelector(JSONObject param) {
        String feignResp = this.sysTransferFeign.getTransferUserSelector(JSONUtil.toJsonStr(param));
        return (Page<JSONObject>) JSONUtil.toBean(feignResp, Page.class);
    }
}
