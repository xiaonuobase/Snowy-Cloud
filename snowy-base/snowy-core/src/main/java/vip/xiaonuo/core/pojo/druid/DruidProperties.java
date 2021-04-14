package vip.xiaonuo.core.pojo.druid;

import cn.hutool.log.Log;
import vip.xiaonuo.common.enums.DbIdEnum;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>数据库数据源配置</p>
 * <p>说明:类中属性包含默认值的不要在这里修改,应该在"application.yml"中配置</p>
 *
 * @author yubaoshan
 * @date 2017/5/21 11:18
 */
@Data
public class DruidProperties {

    private static final Log log = Log.get();

    /**
     * oracle校验语句
     */
    private final String ORACLE_VALIDATE_QUERY_SQL = "select 1 from dual";

    /**
     * postgresql校验语句
     */
    private final String POSTGRESQL_VALIDATE_QUERY_SQL = "select version()";

    /**
     * sqlserver校验语句
     */
    private final String SQLSERVER_VALIDATE_QUERY_SQL = "select 1";

    /**
     * mysql校验语句
     */
    private final String MYSQL_VALIDATE_QUERY_SQL = "select 1";

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private Integer initialSize = 2;

    private Integer minIdle = 1;

    private Integer maxActive = 20;

    private Integer maxWait = 60000;

    private Integer timeBetweenEvictionRunsMillis = 60000;

    private Integer minEvictableIdleTimeMillis = 300000;

    private String validationQuery;

    private Boolean testWhileIdle = true;

    private Boolean testOnBorrow = true;

    private Boolean testOnReturn = true;

    private Boolean poolPreparedStatements = true;

    private Integer maxPoolPreparedStatementPerConnectionSize = 20;

    private String filters = "stat,wall";

    private String dataSourceName;

    public void config(DruidDataSource dataSource) {

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setDriverClassName(driverClassName);
        //定义初始连接数
        dataSource.setInitialSize(initialSize);
        //最小空闲
        dataSource.setMinIdle(minIdle);
        //定义最大连接数
        dataSource.setMaxActive(maxActive);
        //最长等待时间
        dataSource.setMaxWait(maxWait);

        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        //配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(getValidateQueryByUrl(url));
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);

        //打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            log.error(">>> 数据库连接池初始化异常：{}", e.getMessage());
        }
    }

    public Properties createProperties() {
        Properties properties = new Properties();
        properties.put("url", this.url);
        properties.put("username", this.username);
        properties.put("password", this.password);
        properties.put("driverClassName", this.driverClassName);
        properties.put("initialSize", this.initialSize);
        properties.put("maxActive", this.maxActive);
        properties.put("minIdle", this.minIdle);
        properties.put("maxWait", this.maxWait);
        properties.put("poolPreparedStatements", this.poolPreparedStatements);
        properties.put("maxPoolPreparedStatementPerConnectionSize", this.maxPoolPreparedStatementPerConnectionSize);
        properties.put("validationQuery", getValidateQueryByUrl(this.url));
        properties.put("testOnBorrow", this.testOnBorrow);
        properties.put("testOnReturn", this.testOnReturn);
        properties.put("testWhileIdle", this.testWhileIdle);
        properties.put("timeBetweenEvictionRunsMillis", this.timeBetweenEvictionRunsMillis);
        properties.put("minEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
        properties.put("filters", this.filters);
        return properties;
    }

    private String getValidateQueryByUrl(String url) {
        if (url.contains(DbIdEnum.ORACLE.getName())) {
            return ORACLE_VALIDATE_QUERY_SQL;
        } else if (url.contains(DbIdEnum.PG_SQL.getName())) {
            return POSTGRESQL_VALIDATE_QUERY_SQL;
        } else if (url.contains(DbIdEnum.MS_SQL.getName())) {
            return SQLSERVER_VALIDATE_QUERY_SQL;
        } else {
            return MYSQL_VALIDATE_QUERY_SQL;
        }
    }

}
