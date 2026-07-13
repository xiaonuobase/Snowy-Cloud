package vip.xiaonuo.dev.api.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.feign.DevConfigFeign;

/**
 * 配置API上下文Bean
 *
 * @author yubaoshan
 * @date 2025/8/6 21:18
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevConfigApiContextBean implements DevConfigApi {

    final private DevConfigFeign devConfigFeign;

    /**
     * 根据键获取值
     *
     * @author yubaoshan
     * @date 2025/8/6 21:18
     **/
    @Override
    public String getValueByKey(String key) {
        return devConfigFeign.getValueByKey(key);
    }
}
