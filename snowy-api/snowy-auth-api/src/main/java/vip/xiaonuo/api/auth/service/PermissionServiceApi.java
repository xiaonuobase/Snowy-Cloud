package vip.xiaonuo.api.auth.service;

import vip.xiaonuo.common.consts.FeignConstant;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : dongxiayu
 * @classname : PermissionServiceApi
 * @date : 2021/4/8 16:13
 */
@RequestMapping("/feign/permissionServiceApi")
public interface PermissionServiceApi {

    String APP_NAME = FeignConstant.MAIN_APP;

    /**
     * 获取所有权限信息
     *
     * @return 所有权限信息
     * @author dongxiayu
     * @date 2021/4/8 17:51
     */
    @RequestMapping("/getAllPermission")
    List<String> getAllPermission();


}
