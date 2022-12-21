package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 组织Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 23:51
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysOrgFeign")
public interface SysOrgFeign {

    /**
     * 根据id获取名称
     *
     * @author dongxiayu
     * @date 2022/8/4 10:12
     **/
    @RequestMapping("/feign/sys/org/getNameById")
    String getNameById(@RequestParam(value = "orgId",required = false) String orgId);

    /**
     * 根据组织id获取部门主管id
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/org/getSupervisorIdByOrgId")
    String getSupervisorIdByOrgId(@RequestParam(value = "orgId",required = false) String orgId);

    /**
     * 获取组织树选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:46
     **/
    @RequestMapping("/feign/sys/org/orgTreeSelector")
    String orgTreeSelector();

    /**
     * 获取组织列表选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:45
     **/
    @RequestMapping("/feign/sys/org/orgListSelector")
    String orgListSelector(@RequestParam(value = "parentId",required = false) String parentId);

}
