package vip.xiaonuo.main;

import cn.hutool.log.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SpringBoot方式启动类
 *
 * @author yubaoshan
 * @date 2017/5/21 12:06
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(
        scanBasePackages = {
        "vip.xiaonuo.sys",
        "vip.xiaonuo.main",
        "vip.xiaonuo"})
public class SnowyMainApp {

    private static final Log log = Log.get();

    public static void main(String[] args) {
        SpringApplication.run(SnowyMainApp.class, args);
    }

}
