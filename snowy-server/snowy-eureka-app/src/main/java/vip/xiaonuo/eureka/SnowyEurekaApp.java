package vip.xiaonuo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * SpringBoot方式 XiaoNuoEurekaApp 启动类
 *
 * @author dongxiayu
 * @date 2020/12/11 12:06
 */
@EnableEurekaServer
@SpringBootApplication
public class SnowyEurekaApp {
	public static void main(String[] args) {
		SpringApplication.run(SnowyEurekaApp.class, args);
	}

}
