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
     * @author xuyuxiang
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
    String userSelector(@RequestParam("orgId") String orgId, @RequestParam("searchKey") String searchKey);

}
