package vip.xiaonuo.gateway.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.log.Log;
import vip.xiaonuo.common.enums.CommonStatusEnum;
import vip.xiaonuo.common.exception.ServiceException;
import vip.xiaonuo.common.context.constant.ConstantContext;
import vip.xiaonuo.common.exception.enums.ServerExceptionEnum;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 初始化常量的监听器
 * <p>
 * 当spring装配好配置后，就去数据库读constants
 *
 * @author yubaoshan
 * @date 2020/6/6 23:39
 */
public class ConstantsInitListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {

    private static final Log log = Log.get();

    private static final String CONFIG_LIST_SQL = "select code,value from sys_config where status = ?";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
        ConfigurableEnvironment environment = applicationContextInitializedEvent.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername)) {
            throw new ServiceException(ServerExceptionEnum.SERVER_ERROR);
        }

        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            assert dataSourceUrl != null;
            conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);

            // 获取sys_config表的数据
            List<Entity> entityList = SqlExecutor.query(conn, CONFIG_LIST_SQL, new EntityListHandler(), CommonStatusEnum.ENABLE.getCode());

            // 将查询到的参数配置添加到缓存
            if (ObjectUtil.isNotEmpty(entityList)) {
                entityList.forEach(sysConfig ->
                        ConstantContext.putConstant(
                                sysConfig.getStr("code") == null ? sysConfig.getStr("CODE") : sysConfig.getStr("code"),
                                sysConfig.getStr("value") == null ? sysConfig.getStr("VALUE") : sysConfig.getStr("value")
                        )
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error(">>> 读取数据库constants配置信息出错：{}", e.getMessage());
            throw new ServiceException(ServerExceptionEnum.SERVER_ERROR);
        } finally {
            DbUtil.close(conn);
        }

    }
}
