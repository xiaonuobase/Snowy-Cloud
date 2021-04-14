package vip.xiaonuo.gateway.config;

import vip.xiaonuo.gateway.core.filter.AccessFilter;
import vip.xiaonuo.gateway.core.filter.CorsFilter;
import vip.xiaonuo.gateway.core.filter.RequestNoFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *
 * @author dongxiayu
 * @date 2021-03-12 23:30
 */
@Configuration
public class FilterConfig {

    private static final String MAX_AGE = "18000L";

    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter();
    }

    /**
     * 请求号header过滤器
     *
     * @author dongxiayu
     * @date 2021-03-12 23:30
     */
    @Bean
    public RequestNoFilter requestNoFilter() {
        return new RequestNoFilter();
    }

    /**
     * token校验过滤器
     *
     * @author dongxiayu
     * @date 2021-03-12 23:30
     */
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

}
