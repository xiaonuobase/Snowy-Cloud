package vip.xiaonuo.sys.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import vip.xiaonuo.sys.core.mybatis.dbid.SnowyDatabaseIdProvider;
import vip.xiaonuo.sys.core.mybatis.fieldfill.CustomMetaObjectHandler;
import vip.xiaonuo.sys.core.mybatis.sqlfilter.DemoProfileSqlInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis扩展插件配置
 *
 * @author xuyuxiang
 * @date 2020/3/18 10:49
 */
@Configuration
@MapperScan(basePackages = {"vip.xiaonuo.**.mapper"})
public class MybatisConfig {

    /**
     * mybatis-plus分页插件
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 演示环境的sql拦截器
     * <p>
     * 演示环境只开放查询操作，其他都不允许
     *
     * @author yubaoshan
     * @date 2020/5/5 12:24
     */
    @Bean
    public DemoProfileSqlInterceptor demoProfileSqlInterceptor() {
        return new DemoProfileSqlInterceptor();
    }

    /**
     * 自定义公共字段自动注入
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库id选择器
     *
     * @author yubaoshan
     * @date 2020/6/20 21:23
     */
    @Bean
    public SnowyDatabaseIdProvider snowyDatabaseIdProvider() {
        return new SnowyDatabaseIdProvider();
    }

}
