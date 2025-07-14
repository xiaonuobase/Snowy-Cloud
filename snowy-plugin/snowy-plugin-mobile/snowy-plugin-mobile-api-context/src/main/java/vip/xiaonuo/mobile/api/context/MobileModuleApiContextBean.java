package vip.xiaonuo.mobile.api.context;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.mobile.api.MobileModuleApi;
import vip.xiaonuo.mobile.feign.MobileModuleFeign;

import java.util.List;

/**
 * 系统模块ModuleAPI上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MobileModuleApiContextBean implements MobileModuleApi {

    final private MobileModuleFeign mobileModuleFeign;

    /**
     * 获取移动端模块选择器
     *
     * @author xuyuxiang
     * @date 2023/7/15 22:01
     **/
    @Override
    public List<JSONObject> mobileModuleSelector() {
        return this.mobileModuleFeign.mobileModuleSelector();
    }
}
