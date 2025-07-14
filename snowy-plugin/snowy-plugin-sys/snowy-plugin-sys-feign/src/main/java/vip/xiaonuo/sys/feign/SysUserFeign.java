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
 * 用户Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 16:12
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysUserFeign")
public interface SysUserFeign {

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getUserByIdWithoutException")
    String getUserByIdWithoutException(@RequestParam(value = "userId",required = false) String userId);

    /**
     * 根据用户id获取用户对象列表，没有的则为空，都没有则返回空集合
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getUserListByIdListWithoutException")
    String getUserListByIdListWithoutException(@RequestParam(value = "userIdList",required = false) List<String> userIdList);

    /**
     * 根据用户id获取用户对象，没有则抛出异常
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getUserByIdWithException")
    String getUserByIdWithException(@RequestParam(value = "userId",required = false) String userId);

    /**
     * 根据用户id获取用户对象列表，只要有一个没有则抛出异常
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getUserListByIdWithException")
    String getUserListByIdWithException(@RequestParam(value = "userIdList",required = false) List<String> userIdList);

    /**
     * 获取用户拥有角色
     *
     * @author dongxiayu
     * @date 2022/5/13 21:00
     */
    @RequestMapping("/feign/sys/user/ownRole")
    String ownRole(@RequestParam(value = "userId",required = false) String userId);

    /**
     * 给用户授权角色
     *
     * @author dongxiayu
     * @date 2022/8/1 18:28
     */
    @RequestMapping("/feign/sys/user/grantRole")
    void grantRole(@RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "roleIdList",required = false) List<String> roleIdList);

    /**
     * 根据组织id集合获取组织下用户id集合
     *
     * @author dongxiayu
     * @date 2022/6/6 11:40
     **/
    @RequestMapping("/feign/sys/user/getUserIdListByOrgIdList")
    String getUserIdListByOrgIdList(@RequestParam(value = "orgIdList",required = false) List<String> orgIdList);

    /**
     * 根据职位id集合获取职位下用户id集合
     *
     * @author dongxiayu
     * @date 2022/6/6 11:44
     **/
    @RequestMapping("/feign/sys/user/getUserIdListByPositionIdList")
    String getUserIdListByPositionIdList(@RequestParam(value = "positionIdList",required = false) List<String> positionIdList);

    /**
     * 根据用户id和组织id和职位id和主管层级获取上级主管id
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/user/getSupervisorIdBySupervisorLevel")
    String getSupervisorIdBySupervisorLevel(@RequestParam(value = "userIdList",required = false) List<String> userIdList, @RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "supervisorLevel",required = false) String supervisorLevel);

    /**
     * 根据用户id和组织id和职位id和终点主管层级获取上级主管id集合
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/user/getMulSupervisorIdListByEndLevel")
    List<String> getMulSupervisorIdListByEndLevel(@RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "endLevel",required = false) String endLevel);

    /**
     * 获取用户选择器
     *
     * @author dongxiayu
     * @date 2022/4/24 20:08
     */
    @RequestMapping("/feign/sys/user/userSelector")
    String userSelector(@RequestParam(value = "current",required = false) Integer current, @RequestParam(value = "size",required = false) Integer size,@RequestParam("orgId") String orgId, @RequestParam("searchKey") String searchKey);

    /**
     * 获取用户列表（排除当前用户）
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/listUserWithoutCurrent")
    String listUserWithoutCurrent();

    /**
     * 获取用户的职位列表
     *
     * @author dongxiayu
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getPositionListByUserId")
    String getPositionListByUserId(@RequestParam(value = "userId") String userId);
}
