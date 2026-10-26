package io.github.ark85.study.socialnetwork.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tarantool")
@Data
public class TarantoolProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
