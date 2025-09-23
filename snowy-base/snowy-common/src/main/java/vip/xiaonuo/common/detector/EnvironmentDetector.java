package vip.xiaonuo.common.detector;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import vip.xiaonuo.common.prop.CommonProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 环境探测器
 *
 * @author jiangcs
 * @since 2025/9/13 23:20
 */
@Configuration
public class EnvironmentDetector implements ApplicationListener<ApplicationReadyEvent> {

    // 是否是SnowyCloud
    private static boolean snowyCloud = false;

    @Resource
    private Environment environment;
    @Resource
    private CommonProperties commonProperties;

    @Override
    public void onApplicationEvent(@Nullable ApplicationReadyEvent event) {
        // 检查是否存在 Nacos 相关配置
        boolean hasNacosConfig = environment.containsProperty("spring.cloud.nacos.config.server-addr");
        boolean hasNacosDiscovery = environment.containsProperty("spring.cloud.nacos.discovery.server-addr");
        snowyCloud = hasNacosConfig || hasNacosDiscovery;
    }

    /**
     * 转换后端路径
     * <p>Cloud版本会匹配网关路由，返回拼接路径；单体版本直接返回原路径</p>
     *
     * @param path 路径
     * @return 路径
     */
    public String convertBackendPath(String path) {
        if (snowyCloud) {
            List<Map<String, String>> list = commonProperties.getBackendPaths();
            Set<Map<String, String>> urlSet = list.stream().filter(m -> path.startsWith(m.get("name")))
                    .collect(Collectors.toSet());
            if (ObjectUtil.isNotEmpty(urlSet)) {
                return urlSet.iterator().next().get("value") + path;
            }
        }
        return path;
    }

}
