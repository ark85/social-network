package io.github.ark85.study.socialnetwork.configuration;

import com.zaxxer.hikari.HikariDataSource;
import io.github.ark85.study.socialnetwork.datasource.ReplicationRoutingDataSource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class ReplicationRoutingConfiguration {
    @Primary
    @Bean
    @DependsOn({"writeDataSource", "readDataSource", "routingDataSource"})
    public DataSource dataSource(
            @Qualifier("readDataSourceProperties") DataSourceProperties readDataSourceProperties,
            @Qualifier("writeDataSourceProperties") DataSourceProperties writeDataSourceProperties
    ) {
        return new LazyConnectionDataSourceProxy(routingDataSource(
                readDataSourceProperties, writeDataSourceProperties));
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier("readDataSourceProperties") DataSourceProperties readDataSourceProperties,
            @Qualifier("writeDataSourceProperties") DataSourceProperties writeDataSourceProperties
    ) {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("read", readDataSource(readDataSourceProperties));
        dataSourceMap.put("write", writeDataSource(writeDataSourceProperties));
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource(writeDataSourceProperties));

        return routingDataSource;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.read")
    public DataSourceProperties readDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.read.hikari")
    public HikariDataSource readDataSource(@Qualifier("readDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.write")
    public DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.write.hikari")
    public HikariDataSource writeDataSource(@Qualifier("writeDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
