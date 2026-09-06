package io.github.ark85.study.socialnetwork.configuration;

import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.cache.PostEvent;
import io.github.ark85.study.socialnetwork.service.cache.PostEventConsumer;
import io.github.ark85.study.socialnetwork.utils.RedisStreamsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Post> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Post> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, PostEvent>>
    streamContainer(RedisConnectionFactory connectionFactory, PostEventConsumer postEventConsumer) {
        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofSeconds(1)).targetType(PostEvent.class).build();
        var container = StreamMessageListenerContainer.create(
                connectionFactory,
                options
        );

        container.receive(
                Consumer.from(
                        RedisStreamsConstants.CONSUMER_GROUP,
                        RedisStreamsConstants.CONSUMER_NAME
                ),
                StreamOffset.create(
                        RedisStreamsConstants.POST_EVENTS,
                        ReadOffset.lastConsumed()
                ),
                postEventConsumer::handlePostEvent
        );
        container.start();
        return container;
    }
}
