package vip.xiaonuo.dev.api.context;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.dev.api.DevApi;
import vip.xiaonuo.dev.feign.DevFeign;

/**
 * 开发工具综合API上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevApiContextBean implements DevApi {

    final private DevFeign devFeign;

    /**
     * 获得dev模块运维数据
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     **/
    @Override
    public JSONObject getDevOpCount() {
        return this.devFeign.getDevOpCount();
    }

    /**
     * 获得dev工具数量（短信、邮件、文件、消息）
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     **/
    @Override
    public JSONObject getToolDataCount() {
        return this.getToolDataCount();
    }
}
