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
package vip.xiaonuo.biz.core.context.sys;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.sys.api.SysUserApi;
import vip.xiaonuo.sys.feign.SysUserFeign;

import java.util.List;

/**
 * 用户Api上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 16:29
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysUserApiContextBean implements SysUserApi {

    private final SysUserFeign sysUserFeign;

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    public JSONObject getUserByIdWithoutException(String userId) {
        String feignResp = sysUserFeign.getUserByIdWithoutException(userId);
        JSONObject resp = JSONUtil.parseObj(feignResp);
        return resp;
    }

    /**
     * 根据用户id获取用户对象列表，没有的则为空，都没有则返回空集合
     *
     * @param userIdList
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    public List<JSONObject> getUserListByIdListWithoutException(List<String> userIdList) {
        String feignResp = sysUserFeign.getUserListByIdListWithoutException(userIdList);
        JSONArray jsonArray = new JSONArray(feignResp);
        List<JSONObject> resp = jsonArray.toList(JSONObject.class);
        return resp;
    }

    /**
     * 根据用户id获取用户对象，没有则抛出异常
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    public JSONObject getUserByIdWithException(String userId) {
        String feignResp = sysUserFeign.getUserByIdWithoutException(userId);
        JSONObject resp = JSONUtil.parseObj(feignResp);
        return resp;
    }

    /**
     * 根据用户id获取用户对象列表，只要有一个没有则抛出异常
     *
     * @param userIdList
     * @author dongxiayu
     * @date 2022/6/20 18:19
     */
    @Override
    public List<JSONObject> getUserListByIdWithException(List<String> userIdList) {
        String feignResp = sysUserFeign.getUserListByIdWithException(userIdList);
        JSONArray jsonArray = new JSONArray(feignResp);
        List<JSONObject> resp = jsonArray.toList(JSONObject.class);
        return resp;
    }

    /**
     * 获取用户拥有角色
     *
     * @param userId
     * @author dongxiayu
     * @date 2022/5/13 21:00
     */
    @Override
    public List<String> ownRole(String userId) {
        String feignResp = sysUserFeign.ownRole(userId);
        JSONArray jsonArray = new JSONArray(feignResp);
        List<String> resp = jsonArray.toList(String.class);
        return resp;
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
    public void grantRole(String userId, List<String> roleIdList) {
        sysUserFeign.grantRole(userId, roleIdList);
    }

    /**
     * 根据组织id集合获取组织下用户id集合
     *
     * @param orgIdList
     * @author dongxiayu
     * @date 2022/6/6 11:40
     */
    @Override
    public List<String> getUserIdListByOrgIdList(List<String> orgIdList) {
        String feignResp = sysUserFeign.getUserIdListByOrgIdList(orgIdList);
        JSONArray jsonArray = new JSONArray(feignResp);
        List<String> resp = jsonArray.toList(String.class);
        return resp;
    }

    /**
     * 根据职位id集合获取职位下用户id集合
     *
     * @param positionIdList
     * @author dongxiayu
     * @date 2022/6/6 11:44
     */
    @Override
    public List<String> getUserIdListByPositionIdList(List<String> positionIdList) {
        String feignResp = sysUserFeign.getUserIdListByPositionIdList(positionIdList);
        JSONArray jsonArray = new JSONArray(feignResp);
        List<String> resp = jsonArray.toList(String.class);
        return resp;
    }

    /**
     * 根据用户id和组织id和职位id和主管层级获取上级主管id
     *
     * @param userIdList
     * @param userId
     * @param orgId
     * @param supervisorLevel
     * @author xuyuxiang
     * @date 2022/6/6 14:50
     */
    @Override
    public JSONObject getSupervisorIdBySupervisorLevel(List<String> userIdList, String userId, String orgId, String supervisorLevel) {
        String feignResp = sysUserFeign.getSupervisorIdBySupervisorLevel(userIdList, userId, orgId, supervisorLevel);
        JSONObject resp = JSONUtil.parseObj(feignResp);
        return resp;
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
        return sysUserFeign.getMulSupervisorIdListByEndLevel(userId, orgId, endLevel);
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
    public Page<JSONObject> userSelector(String orgId, String searchKey) {
        String feignResp = sysUserFeign.userSelector(orgId, searchKey);
        Page<JSONObject> resp =  (Page<JSONObject>)JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }
}