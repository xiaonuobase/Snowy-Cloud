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
package vip.xiaonuo.biz.api.context.transfer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import vip.xiaonuo.biz.api.BizOrgDirectorTransferApi;
import vip.xiaonuo.sys.provider.SysTransferResourceProvider;

import java.util.Arrays;
import java.util.List;

/**
 * 机构主管权限转移远程提供者，用于 sys 服务以 Feign 方式调用 biz 服务完成机构主管资源的转移
 *
 * @author yubaoshan
 * @date 2026/05/17
 **/
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(name = "bizOrgDirectorTransferApiProvider")
public class BizOrgDirectorRemoteTransferProvider implements SysTransferResourceProvider {

    private final BizOrgDirectorTransferApi bizOrgDirectorTransferApi;

    @Override
    public String getResourceType() {
        return "ORG_DIRECTOR";
    }

    @Override
    public String getResourceTypeName() {
        return "机构主管";
    }

    @Override
    public int getOrder() {
        return 25;
    }

    @Override
    public List<JSONObject> getDetailColumns() {
        return Arrays.asList(
                JSONUtil.createObj().set("title", "机构名称").set("dataIndex", "name"),
                JSONUtil.createObj().set("title", "机构编码").set("dataIndex", "code")
        );
    }

    @Override
    public long getCount(String userId) {
        return bizOrgDirectorTransferApi.getOrgDirectorCount(userId);
    }

    @Override
    public List<JSONObject> getDetailList(String userId) {
        return bizOrgDirectorTransferApi.getOrgDirectorDetailList(userId);
    }

    @Override
    public void executeTransfer(String sourceUserId, String targetUserId, String transferMode,
                                boolean transferAll, List<String> selectedIds) {
        bizOrgDirectorTransferApi.executeOrgDirectorTransfer(sourceUserId, targetUserId, transferMode,
                transferAll, selectedIds);
    }
}
