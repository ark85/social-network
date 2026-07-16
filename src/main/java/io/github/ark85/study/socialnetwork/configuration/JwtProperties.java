package io.github.ark85.study.socialnetwork.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtProperties {
    private String secret;
    private Duration accessTokenTtl;
}
