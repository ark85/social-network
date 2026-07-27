package io.github.ark85.study.socialnetwork.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "social-network.import")
@Data
public class ImportProperties {
    private int batchSize;
}
