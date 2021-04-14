package vip.xiaonuo.sys.config;

import cn.hutool.core.collection.CollectionUtil;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

/**
 * Druid配置
 *
 * @author yubaoshan
 * @date 2017/5/20 21:58
 */
@Configuration
// @Import(MultiDataSourceConfig.class)
public class DataSourceConfig {

    /**
     * druid监控，配置StatViewServlet
     *
     * @author xuyuxiang
     * @date 2020/6/28 16:03
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServletRegistration() {

        // 设置servelet的参数
        HashMap<String, String> statViewServletParams = CollectionUtil.newHashMap();
        statViewServletParams.put("resetEnable", "true");
        statViewServletParams.put("loginUsername", ConstantContextHolder.getDruidMonitorUsername());
        statViewServletParams.put("loginPassword", ConstantContextHolder.getDruidMonitorPassword());

        ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<>(new StatViewServlet());
        registration.addUrlMappings("/druid/*");
        registration.setInitParameters(statViewServletParams);
        return registration;
    }

}
