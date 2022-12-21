package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 系统模块综合Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:29
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysFeign")
public interface SysFeign {

    /**
     * 初始化ID类型的租户系统模块数据
     *
     * @author dongxiayu
     * @date 2022/9/26 14:25
     **/
    @RequestMapping("/feign/sys/initTenDataForCategoryId")
    void initTenDataForCategoryId(@RequestParam(value = "tenId",required = false) String tenId, @RequestParam(value = "tenName",required = false) String tenName);

    /**
     * 删除ID类型的租户系统模块数据
     *
     * @author dongxiayu
     * @date 2022/9/26 14:25
     **/
    @RequestMapping("/feign/sys/removeTenDataForCategoryId")
    void removeTenDataForCategoryId(@RequestParam(value = "tenId",required = false) String tenId);

}