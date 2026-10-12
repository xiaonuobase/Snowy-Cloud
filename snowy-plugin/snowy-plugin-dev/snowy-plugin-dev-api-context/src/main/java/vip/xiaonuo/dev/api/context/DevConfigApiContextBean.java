package vip.xiaonuo.dev.api.context;

import cn.hutool.json.JSONUtil;
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

    /**
     * 根据键设置值，键不存在时抛异常
     *
     * @author yubaoshan
     * @date 2026/10/12 10:00
     **/
    @Override
    public void setValueByKey(String key, String value) {
        // 键值放进 JSON 请求体传递，避免长配置值进 URL，且能区分空字符串与 null
        devConfigFeign.setValueByKey(JSONUtil.toJsonStr(JSONUtil.createObj().set("key", key).set("value", value)));
    }
}
