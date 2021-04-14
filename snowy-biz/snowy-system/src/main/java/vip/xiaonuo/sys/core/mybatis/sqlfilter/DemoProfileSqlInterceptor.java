package vip.xiaonuo.sys.core.mybatis.sqlfilter;

import vip.xiaonuo.common.consts.SpringSecurityConstant;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.common.exception.DemoException;
import vip.xiaonuo.core.util.HttpServletUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.AntPathMatcher;

import java.sql.Connection;

/**
 * 演示环境的sql过滤器，只放开select语句，其他语句都不放过
 *
 * @author yubaoshan
 * @date 2020/5/5 12:21
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DemoProfileSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 演示环境没开，直接跳过此过滤器
        if (!ConstantContextHolder.getDemoEnvFlag()) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        if (SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        } else {

            //放开不进行安全过滤的接口
            for (String notAuthResource : SpringSecurityConstant.NONE_SECURITY_URL_PATTERNS) {
                AntPathMatcher antPathMatcher = new AntPathMatcher();
                if (antPathMatcher.match(notAuthResource, HttpServletUtil.getRequest().getRequestURI())) {
                    return invocation.proceed();
                }
            }
            throw new DemoException();
        }
    }

}
