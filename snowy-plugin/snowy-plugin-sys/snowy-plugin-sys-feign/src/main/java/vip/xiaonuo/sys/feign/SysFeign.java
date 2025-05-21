package vip.xiaonuo.sys.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 系统模块综合Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:29
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "SysFeign")
public interface SysFeign {

    /**
     * 获取系统默认密码
     *
     * @author xuyuxiang
     * @date 2022/9/26 14:25
     **/
    @RequestMapping("/feign/sys/getDefaultPassword")
    String getDefaultPassword();
}
