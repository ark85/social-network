package io.github.ark85.study.socialnetwork.configuration;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "io.github.ark85.study.socialnetwork.client")
@Configuration
@Slf4j
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestIdInterceptor() {
        return template -> {
            String requestId = MDC.get(RequestIdFilter.MDC_KEY);
            if (requestId != null) {
                template.header(RequestIdFilter.HEADER_NAME, requestId);
                log.info("Forwarding {} {}", template.method(), template.url());
            }
        };
    }
}
