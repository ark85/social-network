package io.github.ark85.study.socialnetwork.configuration;

import org.springframework.boot.amqp.autoconfigure.RabbitProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final RabbitProperties brokerProperties;
    private final RabbitMQProperties rabbitProperties;

    public WebSocketConfiguration(
            RabbitProperties brokerProperties,
            RabbitMQProperties rabbitProperties) {
        this.brokerProperties = brokerProperties;
        this.rabbitProperties = rabbitProperties;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/post");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        String virtualHost = brokerProperties.getVirtualHost() != null ? brokerProperties.getVirtualHost() : "/";
        registry.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost(brokerProperties.getHost())
                .setRelayPort(rabbitProperties.getStompPort())
                .setVirtualHost(virtualHost)
                .setClientLogin(brokerProperties.getUsername())
                .setClientPasscode(brokerProperties.getPassword())
                .setSystemLogin(brokerProperties.getUsername())
                .setSystemPasscode(brokerProperties.getPassword())
                .setUserDestinationBroadcast("/topic/unresolved-user-destination")
                .setUserRegistryBroadcast("/topic/user-registry");
        registry.setUserDestinationPrefix("/user");
    }
}
