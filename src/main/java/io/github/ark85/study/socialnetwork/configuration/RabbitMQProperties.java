package io.github.ark85.study.socialnetwork.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit")
@Data
public class RabbitMQProperties {
    private String postRoutingKeyPrefix;
    private int postQueueNumber;
    private int stompPort;
}
