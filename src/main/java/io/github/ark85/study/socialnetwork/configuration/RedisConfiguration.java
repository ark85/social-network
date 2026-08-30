package io.github.ark85.study.socialnetwork.configuration;

import io.github.ark85.study.socialnetwork.model.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Post> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Post> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
