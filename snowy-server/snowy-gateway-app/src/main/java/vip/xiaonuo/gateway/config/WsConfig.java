package vip.xiaonuo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.socket.client.TomcatWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;

/**
 * ws配置
 *
 * @author yubaoshan
 * @date 2025-08-05 23:30
 */
@Configuration
public class WsConfig {

    @Bean
    @Primary
    WebSocketClient tomcatWebSocketClient(){
        return new TomcatWebSocketClient();
    }

    @Bean
    @Primary
    public RequestUpgradeStrategy requestUpgradeStrategy() {
        return new TomcatRequestUpgradeStrategy();
    }
}
