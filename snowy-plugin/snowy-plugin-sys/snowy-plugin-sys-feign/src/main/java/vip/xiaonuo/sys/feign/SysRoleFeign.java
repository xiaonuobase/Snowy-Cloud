package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * 角色Feign
 *
 * @author dongxiayu
 * @date 2022/11/21 18:34
 **/
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysRoleFeign")
public interface SysRoleFeign {

    /**
     * 判断组织下是否存在角色
     *
     * @author dongxiayu
     * @date 2022/8/2 11:16
     */
    @RequestMapping("/feign/sys/role/orgHasRole")
    boolean orgHasRole(@RequestParam(value = "orgIdList",required = false) List<String> orgIdList);

    /**
     * 获取角色选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:49
     **/
    @RequestMapping("/feign/sys/role/roleSelector")
    String roleSelector(@RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "category",required = false) String category, @RequestParam(value = "searchKey",required = false) String searchKey,@RequestParam(value = "dataScopeList",required = false)  List<String> dataScopeList,@RequestParam(value = "excludeSuperAdmin",required = false) boolean excludeSuperAdmin);

    /**
     * 代码生成菜单按钮授权
     *
     * @author dongxiayu
     * @date 2022/11/1 15:58
     **/
    @RequestMapping("/feign/sys/role/grantForGenMenuAndButton")
    void grantForGenMenuAndButton(@RequestParam(value = "menuId",required = false) String menuId);

}
