package vip.xiaonuo.config.core.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import vip.xiaonuo.config.core.consts.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Date;

/**
 * @author : dongxiayu
 * @classname : AppStartupListener
 * @description : 启动日志打印
 * @date : 2020/12/9 14:29
 */
@Slf4j
@Component
public class AppStartupListener implements ApplicationRunner {

    /**
     * 上下文对象实例
     */
    @Resource
    private ApplicationContext ctx;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String appName = ctx.getEnvironment().getProperty("spring.application.name");
        String appJvmName = ManagementFactory.getRuntimeMXBean().getName();
        String appHost = InetAddress.getLocalHost().getHostAddress();
        String appPort = ctx.getEnvironment().getProperty("server.port");
        String appPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
        String appStartupDate = DateUtil.format(new Date(ctx.getStartupDate()), DatePattern.NORM_DATETIME_MS_PATTERN);
        log.info(AppConstant.APP_START_INFO,appName,appJvmName.split("@")[0],appStartupDate,appHost,appPort,(appPath==null?"":appPath));
    }
}
