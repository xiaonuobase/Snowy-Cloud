package vip.xiaonuo.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBoot方式启动类
 *
 * @author yubaoshan
 * @date 2017/5/21 12:06
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"vip.xiaonuo"})
public class SnowySampleApp {

    public static void main(String[] args) {
        SpringApplication.run(SnowySampleApp.class, args);
    }

}
