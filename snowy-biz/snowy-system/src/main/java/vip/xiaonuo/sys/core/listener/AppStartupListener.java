/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.sys.core.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import vip.xiaonuo.common.consts.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Date;

/**
 * 启动日志打印
 *
 * @author : dongxiayu
 * @date : 2020/12/9 14:29
 */
@Slf4j
@Component
public class AppStartupListener implements ApplicationRunner, Ordered {

    /**
     * 上下文对象实例
     */
    @Resource
    private ApplicationContext ctx;

    /**
     * Callback used to run the bean.
     *
     * @author : dongxiayu
     * @date : 2020/12/9 14:29
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String appName = ctx.getEnvironment().getProperty("spring.application.name");
        String appJvmName = ManagementFactory.getRuntimeMXBean().getName();
        String appHost = InetAddress.getLocalHost().getHostAddress();
        String appPort = ctx.getEnvironment().getProperty("server.port");
        String appPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
        String appStartupDate = DateUtil.format(new Date(ctx.getStartupDate()), DatePattern.NORM_DATETIME_MS_PATTERN);
        log.info(CommonConstant.APP_START_INFO,appName,appJvmName.split("@")[0],appStartupDate,appHost,appPort,(appPath==null?"":appPath));
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
