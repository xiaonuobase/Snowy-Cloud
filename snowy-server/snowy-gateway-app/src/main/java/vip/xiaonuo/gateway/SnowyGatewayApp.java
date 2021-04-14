package vip.xiaonuo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SpringBoot方式 XiaoNuoGatewayApp 启动类
 * TODO 网关层计划调整为动态路由，路由配置在网关应用启动时进行路由策略初始化，后续可考虑增加业务管理端调整路由策略后实时推送给网关层应用
 * @author dongxiayu
 * @date 2020/12/11 12:06
 */
@EnableFeignClients(basePackages = "vip.xiaonuo.gateway.core.consumer")
@EnableDiscoveryClient
@SpringBootApplication
public class SnowyGatewayApp {
	public static void main(String[] args) {
		SpringApplication.run(SnowyGatewayApp.class, args);
	}

}
