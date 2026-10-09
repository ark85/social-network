package io.github.ark85.study.socialnetwork.service.queue;

import io.github.ark85.study.socialnetwork.configuration.RabbitProperties;
import io.github.ark85.study.socialnetwork.model.queue.PostFeedEvent;
import io.github.ark85.study.socialnetwork.utils.RabbitConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class PostPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueueRouterService rabbitQueueRouterService;
    private final RabbitProperties rabbitProperties;

    public void publishPostFeedEvent(PostFeedEvent postFeedEvent) {
        String routingKey = rabbitQueueRouterService.getRoutingKey(postFeedEvent.getUserId());
        rabbitTemplate.convertAndSend(
                rabbitProperties.getPostRoutingKeyPrefix() + "." + RabbitConstants.EXCHANGE,
                routingKey, postFeedEvent);
    }
}
