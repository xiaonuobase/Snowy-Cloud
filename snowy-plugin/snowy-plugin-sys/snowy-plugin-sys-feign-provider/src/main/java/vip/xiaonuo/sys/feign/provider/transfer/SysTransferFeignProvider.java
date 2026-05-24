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
package vip.xiaonuo.sys.feign.provider.transfer;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.sys.api.SysTransferApi;
import vip.xiaonuo.sys.feign.SysTransferFeign;

/**
 * 权限转移Feign提供者
 *
 * @author yubaoshan
 * @date 2026/05/17
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysTransferFeignProvider implements SysTransferFeign {

    private final SysTransferApi sysTransferApi;

    @Override
    public String getTransferResourceList(String sourceUserId) {
        return JSONUtil.toJsonStr(sysTransferApi.getTransferResourceList(sourceUserId));
    }

    @Override
    public String getTransferResourceDetail(String sourceUserId, String resourceType) {
        return JSONUtil.toJsonStr(sysTransferApi.getTransferResourceDetail(sourceUserId, resourceType));
    }

    @Override
    public void executeTransfer(String executeParam) {
        sysTransferApi.executeTransfer(JSONUtil.parseObj(executeParam));
    }

    @Override
    public String getTransferOrgTreeSelector(String param) {
        return JSONUtil.toJsonStr(sysTransferApi.getTransferOrgTreeSelector(JSONUtil.parseObj(param)));
    }

    @Override
    public String getTransferUserSelector(String param) {
        return JSONUtil.toJsonStr(sysTransferApi.getTransferUserSelector(JSONUtil.parseObj(param)));
    }
}
