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
 * @date 2022/11/22 23:51
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysGroupFeign")
public interface SysGroupFeign {

    /**
     * 根据id获取名称
     *
     * @author dongxiayu
     * @date 2022/8/4 10:12
     **/
    @RequestMapping("/feign/sys/group/ownUser")
    List<String> ownUser(@RequestParam(value = "userId",required = false) String userId);

    /**
     * 根据组织id获取部门主管id
     *
     * @author dongxiayu
     * @date 2022/6/6 14:50
     **/
    @RequestMapping("/feign/sys/group/grantUser")
    void grantUser(@RequestParam(value = "userId",required = false) String userId, @RequestParam(value = "roleIdList",required = false) List<String> roleIdList);
}
