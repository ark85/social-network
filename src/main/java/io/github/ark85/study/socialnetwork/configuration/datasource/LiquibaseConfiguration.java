package io.github.ark85.study.socialnetwork.configuration.datasource;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class LiquibaseConfiguration {
    @Bean
    public SpringLiquibase regularLiquibase(@Qualifier("dataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(
                "classpath:/db/changelog/regular/db.changelog-master.yaml"
        );
        return liquibase;
    }

    @Bean
    public SpringLiquibase citusLiquibase(@Qualifier("citusDataSource") DataSource citusDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(citusDataSource);
        liquibase.setChangeLog(
                "classpath:/db/changelog/citus/db.changelog-master.yaml"
        );
        return liquibase;
    }
}
