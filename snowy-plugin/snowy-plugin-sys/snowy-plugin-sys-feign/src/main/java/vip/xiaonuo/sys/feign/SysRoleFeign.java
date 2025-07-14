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

import cn.hutool.json.JSONObject;
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
    String roleSelector(@RequestParam(value = "current",required = false) Integer current, @RequestParam(value = "size",required = false) Integer size,@RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "category",required = false) String category, @RequestParam(value = "searchKey",required = false) String searchKey, @RequestParam(value = "dataScopeList",required = false) List<String> dataScopeList, @RequestParam(value = "excludeSuperAdmin",required = false) boolean excludeSuperAdmin);

    /**
     * 代码生成菜单按钮授权
     *
     * @author dongxiayu
     * @date 2022/11/1 15:58
     **/
    @RequestMapping("/feign/sys/role/grantForGenMenuAndButton")
    void grantForGenMenuAndButton(@RequestParam(value = "menuId",required = false) String menuId);

    /**
     * 获取资源授权树
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @RequestMapping("/feign/sys/role/resourceTreeSelector")
    List<JSONObject> resourceTreeSelector();

    /**
     * 获取权限授权树
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @RequestMapping("/feign/sys/role/permissionTreeSelector")
    List<String> permissionTreeSelector();
}
