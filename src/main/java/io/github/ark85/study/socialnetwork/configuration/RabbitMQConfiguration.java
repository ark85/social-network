package io.github.ark85.study.socialnetwork.configuration;

import io.github.ark85.study.socialnetwork.utils.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@Slf4j
public class RabbitMQConfiguration {

    @Bean
    public Declarables postFeedDeclarables(RabbitProperties rabbitProperties) {
        List<Declarable> declarables = new ArrayList<>();

        TopicExchange exchange = ExchangeBuilder.topicExchange(
                rabbitProperties.getPostRoutingKeyPrefix() + "." + RabbitConstants.EXCHANGE).durable(true).build();
        declarables.add(exchange);

        for (int i = 0; i < rabbitProperties.getPostQueueNumber(); i++) {
            Queue queue = QueueBuilder.durable(
                    rabbitProperties.getPostRoutingKeyPrefix() + "." + RabbitConstants.QUEUE + "." + i).build();

            Binding binding = BindingBuilder
                    .bind(queue).to(exchange).with(rabbitProperties.getPostRoutingKeyPrefix() + "." + i);

            declarables.add(queue);
            declarables.add(binding);
        }

        return new Declarables(declarables);
    }

    @Bean
    public List<String> postFeedQueueNames(RabbitProperties rabbitProperties) {
        return IntStream.range(0, rabbitProperties.getPostQueueNumber())
                .mapToObj(i -> rabbitProperties.getPostRoutingKeyPrefix() + "." + RabbitConstants.QUEUE + "." + i)
                .toList();
    }
}
