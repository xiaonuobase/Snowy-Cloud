package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 职位Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 23:52
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysPositionFeign")
public interface SysPositionFeign {

    /**
     * 根据id获取名称
     *
     * @author dongxiayu
     * @date 2022/8/4 10:13
     **/
    @RequestMapping("/feign/sys/position/getNameById")
    String getNameById(@RequestParam(value = "positionId",required = false) String positionId);

    /**
     * 获取职位选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:47
     **/
    @RequestMapping("/feign/sys/position/positionSelector")
    String positionSelector(@RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "searchKey",required = false) String searchKey);
}