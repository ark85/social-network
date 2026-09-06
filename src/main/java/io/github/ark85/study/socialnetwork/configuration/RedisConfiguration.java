package io.github.ark85.study.socialnetwork.configuration;

import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.cache.PostEvent;
import io.github.ark85.study.socialnetwork.service.cache.PostEventConsumer;
import io.github.ark85.study.socialnetwork.utils.RedisStreamsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Post> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Post> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.byteArray());
        template.setHashValueSerializer(RedisSerializer.byteArray());
        return template;
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, PostEvent>> streamContainer(
            RedisConnectionFactory connectionFactory,
            PostEventConsumer postEventConsumer,
            RedisTemplate<String, Post> redisTemplate) {
        createConsumerGroup(redisTemplate);
        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .keySerializer(RedisSerializer.string())
                .targetType(PostEvent.class)
                .build();
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

    private void createConsumerGroup(RedisTemplate<String, Post> redisTemplate) {
        try {
            redisTemplate.opsForStream().createGroup(
                    RedisStreamsConstants.POST_EVENTS,
                    ReadOffset.latest(),
                    RedisStreamsConstants.CONSUMER_GROUP
            );
        } catch (RedisSystemException exception) {
            String message = exception.getMostSpecificCause().getMessage();
            if (message != null && message.contains("BUSYGROUP")) {
                log.info("Consumer group {} already exists.", RedisStreamsConstants.CONSUMER_GROUP);
                return;
            }
            throw exception;
        }
    }
}
