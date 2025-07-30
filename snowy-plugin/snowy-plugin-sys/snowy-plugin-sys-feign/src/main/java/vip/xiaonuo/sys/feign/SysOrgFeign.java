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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

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
    List<Tree<String>> orgTreeSelector();

    /**
     * 获取组织列表选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:45
     **/
    @RequestMapping("/feign/sys/org/orgListSelector")
    String orgListSelector(@RequestParam(value = "current",required = false) Integer current, @RequestParam(value = "size",required = false) Integer size,@RequestParam(value = "parentId",required = false) String parentId);

    /**
     * 根据机构id获取父id集合
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/org/getParentIdListByOrgId")
    List<String> getParentIdListByOrgId(@RequestParam(value = "orgId",required = false) String orgId);

    /**
     * 根据机构id集合获取机构数据集合
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/org/getOrgListByIdListWithoutException")
    List<JSONObject> getOrgListByIdListWithoutException(@RequestParam(value = "orgIdList",required = false) List<String> orgIdList);
}
