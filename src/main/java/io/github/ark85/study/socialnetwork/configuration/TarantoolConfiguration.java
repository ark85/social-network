package io.github.ark85.study.socialnetwork.configuration;

import io.tarantool.client.box.TarantoolBoxClient;
import io.tarantool.client.factory.TarantoolFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarantoolConfiguration {

    @Bean(destroyMethod = "close")
    public TarantoolBoxClient tarantoolClient(TarantoolProperties tarantoolProperties) throws Exception {
        return TarantoolFactory.box()
                .withHost(tarantoolProperties.getHost())
                .withPort(tarantoolProperties.getPort())
                .withUser(tarantoolProperties.getUsername())
                .withPassword(tarantoolProperties.getPassword())
                .build();
    }
}
