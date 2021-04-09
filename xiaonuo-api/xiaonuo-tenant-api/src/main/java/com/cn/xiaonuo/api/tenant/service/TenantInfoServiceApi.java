package com.cn.xiaonuo.api.tenant.service;

import com.cn.xiaonuo.api.tenant.entity.TenantInfo;
import com.cn.xiaonuo.common.consts.FeignConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TenantInfoService 服务接口
 *
 * @author dongxiayu
 * @date 2021/3/31 1:26
 */
@RequestMapping("/feign/tenantInfoServiceApi")
public interface TenantInfoServiceApi {

    String APP_NAME = FeignConstant.TENANT_APP;

    /**
     * 获取租户信息，通过租户编码
     *
     * @param code 租户编码
     * @return 租户信息
     * @author xuyuxiang
     * @date 2019-06-19 14:17
     */
    @RequestMapping("/getByCode")
    TenantInfo getByCode(@RequestParam String code);

}