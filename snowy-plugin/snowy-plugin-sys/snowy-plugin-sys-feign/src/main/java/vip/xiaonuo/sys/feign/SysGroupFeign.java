package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * 用户组Feign
 *
 * @author yubaoshan
 * @date 2022/12/25 01:03
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysGroupFeign")
public interface SysGroupFeign {

    /**
     * 根据id获取名称
     *
     * @author yubaoshan
     * @date 2022/12/25 01:03
     **/
    @RequestMapping("/feign/sys/group/ownUser")
    List<String> ownUser(@RequestParam(value = "userId",required = false) String userId);

    /**
     * 根据组织id获取部门主管id
     *
     * @author yubaoshan
     * @date 2022/12/25 01:03
     **/
    @RequestMapping("/feign/sys/group/grantUser")
    void grantUser(@RequestParam(value = "groupId",required = false) String groupId, @RequestParam(value = "userIdList",required = false) List<String> userIdList);

    /**
     * 获取用户组选择器
     *
     * @author yubaoshan
     * @date 2025/1/12 02:36
     */
    @RequestMapping("/feign/sys/group/groupSelector")
    String groupSelector(@RequestParam(value = "searchKey",required = false) String searchKey, @RequestParam(value = "current",required = false) Integer current, @RequestParam(value = "size",required = false) Integer size);
}
