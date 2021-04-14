package vip.xiaonuo.sys.config;

import vip.xiaonuo.common.consts.SpringSecurityConstant;
import vip.xiaonuo.security.filter.security.JwtAuthenticationTokenFilter;
import vip.xiaonuo.security.filter.security.entrypoint.JwtAuthenticationEntryPoint;
import vip.xiaonuo.sys.modular.auth.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * SpringSecurity配置
 * @author xuyuxiang
 * @date 2020/3/18 10:54
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthServiceImpl authService;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 开启跨域访问拦截器
     *
     * @author yubaoshan
     * @date 2020/4/29 9:50
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        httpSecurity.csrf().disable();

        //开启跨域访问
        httpSecurity.cors();

        //不使用默认退出，自定义退出
        httpSecurity.logout().disable();

        //未授权时访问须授权的资源端点
        httpSecurity.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

        //放开一些接口的权限校验
        for (String notAuthResource : SpringSecurityConstant.NONE_SECURITY_URL_PATTERNS) {
            httpSecurity.authorizeRequests().antMatchers(notAuthResource).permitAll();
        }

        //其余的都需授权访问
        httpSecurity.authorizeRequests().anyRequest().authenticated();

        //前置token过滤器
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //用户详情service
        httpSecurity.userDetailsService(authService);

        //全局不创建session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //禁用页面缓存，返回的都是json
        httpSecurity.headers()
                .frameOptions().disable()
                .cacheControl();
    }

}
