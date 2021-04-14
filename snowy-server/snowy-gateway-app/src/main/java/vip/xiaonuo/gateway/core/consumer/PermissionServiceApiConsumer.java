package vip.xiaonuo.gateway.core.consumer;

import vip.xiaonuo.api.auth.service.PermissionServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author : dongxiayu
 * @classname : PermissionServiceApiConsumer
 * @description : 权限服务消费客户端
 * @date : 2021/4/8 17:09
 */
@Component
@FeignClient(name = PermissionServiceApi.APP_NAME,contextId = "PermissionServiceApi")
public interface PermissionServiceApiConsumer extends PermissionServiceApi {
}
