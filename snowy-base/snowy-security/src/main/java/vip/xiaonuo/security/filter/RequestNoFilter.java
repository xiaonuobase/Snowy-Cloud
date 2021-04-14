package vip.xiaonuo.security.filter;


import vip.xiaonuo.common.context.requestno.RequestNoContext;
import vip.xiaonuo.common.consts.CommonConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 对请求生成唯一编码
 *
 * @author yubaoshan
 * @date 2020/6/21 10:04
 */
public class RequestNoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 生成唯一请求号uuid
            String requestNo = UUID.randomUUID().toString();

            // 增加响应头的请求号
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.addHeader(CommonConstant.REQUEST_NO_HEADER_NAME, requestNo);

            // 临时存储
            RequestNoContext.set(requestNo);

            // 放开请求
            chain.doFilter(request, response);

        } finally {
            // 清除临时存储的唯一编号
            RequestNoContext.clear();
        }

    }

    @Override
    public void destroy() {

    }

}
