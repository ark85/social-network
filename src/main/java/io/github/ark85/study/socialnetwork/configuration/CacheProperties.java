package io.github.ark85.study.socialnetwork.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "social-network.cache")
@Data
public class CacheProperties {
    private int cacheSize;
    private String postsCacheKey;
    private Integer postsCacheTimeToLiveHours;
}
