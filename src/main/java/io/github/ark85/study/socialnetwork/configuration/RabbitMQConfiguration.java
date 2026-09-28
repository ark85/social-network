package io.github.ark85.study.socialnetwork.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
@Slf4j
public class RabbitMQConfiguration {
    public static final String FEED_EXCHANGE = "feed.exchange";
    public static final String FEED_QUEUE_PREFIX = "feed.queue.";
    public static final int FEED_SHARDS = 3;

    @Bean
    public TopicExchange feedExchange() {
        return new TopicExchange(FEED_EXCHANGE);
    }

    @Bean
    public List<Queue> feedQueues() {
        return IntStream.range(0, FEED_SHARDS)
                .mapToObj(i ->
                        QueueBuilder
                                .durable(FEED_QUEUE_PREFIX + i)
                                .build()
                )
                .toList();
    }

    @Bean
    public List<Binding> feedBindings(
            TopicExchange feedExchange,
            List<Queue> feedQueues
    ) {
        return IntStream.range(0, FEED_SHARDS)
                .mapToObj(i ->
                        BindingBuilder
                                .bind(feedQueues.get(i))
                                .to(feedExchange)
                                .with("feed." + i)
                )
                .toList();
    }
}

