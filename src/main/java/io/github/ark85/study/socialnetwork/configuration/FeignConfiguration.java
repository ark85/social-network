package io.github.ark85.study.socialnetwork.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "io.github.ark85.study.socialnetwork.client")
@Configuration
public class FeignConfiguration {

}
