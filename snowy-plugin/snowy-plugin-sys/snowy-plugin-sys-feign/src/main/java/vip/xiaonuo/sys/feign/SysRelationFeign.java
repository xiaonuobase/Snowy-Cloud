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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * 关系Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:29
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysRelationFeign")
public interface SysRelationFeign {

    /**
     * 根据角色id集合获取角色下用户id集合
     *
     * @author dongxiayu
     * @date 2022/11/22 22:29
     **/
    @RequestMapping("/feign/sys/relation/getUserIdListByRoleIdList")
    List<String> getUserIdListByRoleIdList(@RequestParam("roleIdList") List<String> roleIdList);

    /**
     * 根据用户组id集合获取用户组下用户id集合
     *
     * @author xuyuxiang
     * @date 2022/6/6 11:43
     **/
    @RequestMapping("/feign/sys/relation/getUserIdListByGroupIdList")
    List<String> getUserIdListByGroupIdList(@RequestParam("groupIdList") List<String> groupIdList);

    /**
     * 根据移动端菜单Id集合移除角色和移动端菜单关系
     *
     * @author xuyuxiang
     * @date 2023/1/31 9:54
     **/
    @RequestMapping("/feign/sys/relation/removeRoleHasMobileMenuRelation")
    void removeRoleHasMobileMenuRelation(@RequestParam("targetIdList") List<String> targetIdList);

    /**
     * 清除对应的角色与移动端菜单信息中的【授权的移动端按钮信息】
     *
     * @author xuyuxiang
     * @date 2023/1/31 9:54
     **/
    @RequestMapping("/feign/sys/relation/removeRoleHasMobileButtonRelation")
    void removeRoleHasMobileButtonRelation(@RequestParam("targetIdList") List<String> targetIdList,
                                           @RequestParam("buttonIdList")  List<String> buttonIdList);

}