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
package vip.xiaonuo.sys.feign.provider.user;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.sys.api.SysUserApi;
import vip.xiaonuo.sys.feign.SysUserFeign;

import java.util.List;

/**
 * 用户Feign提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 16:16
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysUserFeignProvider implements SysUserFeign {

    private final SysUserApi sysUserApi;

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserByIdWithoutException")
    public String getUserByIdWithoutException(@RequestParam(value = "userId",required = false) String userId) {
        return JSONUtil.toJsonStr(sysUserApi.getUserByIdWithoutException(userId));
    }

    /**
     * 根据用户id获取用户对象列表，没有的则为空，都没有则返回空集合
     *
     * @param userIdList
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserListByIdListWithoutException")
    public String getUserListByIdListWithoutException(@RequestParam(value = "userIdList",required = false) List<String> userIdList) {
        return JSONUtil.toJsonStr(sysUserApi.getUserListByIdListWithoutException(userIdList));
    }

    /**
     * 根据用户id获取用户对象，没有则抛出异常
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserByIdWithException")
    public String getUserByIdWithException(@RequestParam(value = "userId",required = false) String userId) {
        return JSONUtil.toJsonStr(sysUserApi.getUserByIdWithException(userId));
    }

    /**
     * 根据用户id获取用户对象列表，只要有一个没有则抛出异常
     *
     * @param userIdList
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserListByIdWithException")
    public String getUserListByIdWithException(@RequestParam(value = "userIdList",required = false) List<String> userIdList) {
        return JSONUtil.toJsonStr(sysUserApi.getUserListByIdWithException(userIdList));
    }

    /**
     * 获取用户拥有角色
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/5/13 21:00
     */
    @Override
    @RequestMapping("/feign/sys/user/ownRole")
    public String ownRole(@RequestParam(value = "userId",required = false) String userId) {
        return JSONUtil.toJsonStr(sysUserApi.ownRole(userId));
    }

    /**
     * 给用户授权角色
     *
     * @param userId
     * @param roleIdList
     * @author dongxiayu
     * @date 2022/8/1 18:28
     */
    @Override
    @RequestMapping("/feign/sys/user/grantRole")
    public void grantRole(@RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "roleIdList",required = false) List<String> roleIdList) {
        sysUserApi.grantRole(userId, roleIdList);
    }

    /**
     * 根据组织id集合获取组织下用户id集合
     *
     * @param orgIdList
     * @author dongxiayu
     * @date 2022/6/6 11:40
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserIdListByOrgIdList")
    public String getUserIdListByOrgIdList(@RequestParam(value = "orgIdList",required = false) List<String> orgIdList) {
        return JSONUtil.toJsonStr(sysUserApi.getUserIdListByOrgIdList(orgIdList));
    }

    /**
     * 根据职位id集合获取职位下用户id集合
     *
     * @param positionIdList
     * @author dongxiayu
     * @date 2022/6/6 11:44
     */
    @Override
    @RequestMapping("/feign/sys/user/getUserIdListByPositionIdList")
    public String getUserIdListByPositionIdList(@RequestParam(value = "positionIdList",required = false) List<String> positionIdList) {
        return JSONUtil.toJsonStr(sysUserApi.getUserIdListByPositionIdList(positionIdList));
    }

    /**
     * 根据用户id和组织id和职位id和主管层级获取上级主管id
     *
     * @param userIdList
     * @param userId
     * @param orgId
     * @param supervisorLevel
     * @author dongxiayu
     * @date 2022/6/6 14:50
     */
    @Override
    public String getSupervisorIdBySupervisorLevel(List<String> userIdList, String userId, String orgId, String supervisorLevel) {
        return JSONUtil.toJsonStr(sysUserApi.getSupervisorIdBySupervisorLevel(userIdList, userId, orgId, supervisorLevel));
    }

    /**
     * 根据用户id和组织id和职位id和终点主管层级获取上级主管id集合
     *
     * @param userId
     * @param orgId
     * @param endLevel
     * @author xuyuxiang
     * @date 2022/6/6 14:50
     */
    @Override
    public List<String> getMulSupervisorIdListByEndLevel(String userId, String orgId, String endLevel) {
        return sysUserApi.getMulSupervisorIdListByEndLevel(userId, orgId, endLevel);
    }

    /**
     * 获取用户选择器
     *
     * @param orgId
     * @param searchKey
     * @author dongxiayu
     * @date 2022/4/24 20:08
     */
    @Override
    @RequestMapping("/feign/sys/user/userSelector")
    public String userSelector(@RequestParam(value = "current",required = false) Integer current, @RequestParam(value = "size",required = false) Integer size,@RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "searchKey",required = false) String searchKey) {
        return JSONUtil.toJsonStr(sysUserApi.userSelector(orgId, searchKey));
    }

    /**
     * 获取用户列表（排除当前用户）
     *
     * @author xuyuxiang
     * @date 2024/9/30 09:39
     **/
    @RequestMapping("/feign/sys/user/listUserWithoutCurrent")
    @Override
    public String listUserWithoutCurrent() {
        return JSONUtil.toJsonStr(sysUserApi.listUserWithoutCurrent());
    }

    /**
     * 获取用户的职位列表
     *
     * @author xuyuxiang
     * @date 2024/9/30 09:39
     **/
    @RequestMapping("/feign/sys/user/getPositionListByUserId")
    @Override
    public String getPositionListByUserId(@RequestParam(value = "userId") String userId) {
        return JSONUtil.toJsonStr(sysUserApi.getPositionListByUserId(userId));
    }

    /**
     * 获取或创建用户扩展信息
     *
     * @author xuyuxiang
     * @date 2022/6/20 18:19
     **/
    @RequestMapping("/feign/sys/user/getOrCreateSysUserExt")
    @Override
    public String getOrCreateSysUserExt(@RequestParam(value = "userId") String userId) {
        return JSONUtil.toJsonStr(sysUserApi.getOrCreateSysUserExt(userId));
    }
}
