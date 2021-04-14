package vip.xiaonuo.actuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBoot方式 XiaoNuoActuatorApp 启动类
 *
 * @author dongxiayu
 * @date 2020/12/11 12:06
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class SnowyActuatorApp {
	public static void main(String[] args) {
		SpringApplication.run(SnowyActuatorApp.class, args);
	}
}
