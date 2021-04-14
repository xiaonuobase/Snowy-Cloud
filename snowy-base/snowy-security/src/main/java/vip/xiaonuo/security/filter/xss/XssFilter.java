package vip.xiaonuo.security.filter.xss;

import vip.xiaonuo.common.context.constant.ConstantContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * xss过滤器
 *
 * @author yubaoshan
 * @date 2020/6/21 10:04
 */
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String servletPath = httpServletRequest.getServletPath();

        // 获取不进行url过滤的接口
        List<String> unXssFilterUrl = ConstantContextHolder.getUnXssFilterUrl();
        if (unXssFilterUrl != null && unXssFilterUrl.contains(servletPath)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }

    @Override
    public void destroy() {

    }

}
