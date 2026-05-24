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
package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 权限转移Feign
 *
 * @author yubaoshan
 * @date 2026/05/17
 **/
@FeignClient(name = FeignConstant.WEB_APP, contextId = "SysTransferFeign")
public interface SysTransferFeign {

    /**
     * 获取用户可转移资源列表
     *
     * @author yubaoshan
     * @date 2026/05/17
     **/
    @RequestMapping("/feign/sys/transfer/getTransferResourceList")
    String getTransferResourceList(@RequestParam(value = "sourceUserId", required = false) String sourceUserId);

    /**
     * 获取资源明细列表
     *
     * @author yubaoshan
     * @date 2026/05/17
     **/
    @RequestMapping("/feign/sys/transfer/getTransferResourceDetail")
    String getTransferResourceDetail(@RequestParam(value = "sourceUserId", required = false) String sourceUserId,
                                     @RequestParam(value = "resourceType", required = false) String resourceType);

    /**
     * 执行权限转移
     *
     * @author yubaoshan
     * @date 2026/05/17
     **/
    @PostMapping("/feign/sys/transfer/executeTransfer")
    void executeTransfer(@RequestBody String executeParam);

    /**
     * 获取机构树选择器
     *
     * @author yubaoshan
     * @date 2026/05/17
     **/
    @PostMapping("/feign/sys/transfer/getTransferOrgTreeSelector")
    String getTransferOrgTreeSelector(@RequestBody String param);

    /**
     * 获取用户选择器
     *
     * @author yubaoshan
     * @date 2026/05/17
     **/
    @PostMapping("/feign/sys/transfer/getTransferUserSelector")
    String getTransferUserSelector(@RequestBody String param);
}
