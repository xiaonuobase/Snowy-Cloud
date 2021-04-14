package vip.xiaonuo.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * SpringBoot方式 XiaoNuoConfigApp 启动类
 *
 * @author dongxiayu
 * @date 2017/5/21 12:06
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class SnowyConfigApp {

	public static void main(String[] args) {
		SpringApplication.run(SnowyConfigApp.class, args);
	}

}
