package io.github.ark85.study.socialnetwork.service.queue;

import io.github.ark85.study.socialnetwork.configuration.RabbitMQProperties;
import io.github.ark85.study.socialnetwork.model.queue.PostFeedEvent;
import io.github.ark85.study.socialnetwork.utils.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class PostPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitQueueRouterService rabbitQueueRouterService;

    private final String exchangeName;

    public PostPublisherService(RabbitTemplate rabbitTemplate,
                                RabbitQueueRouterService rabbitQueueRouterService,
                                RabbitMQProperties rabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitQueueRouterService = rabbitQueueRouterService;
        this.exchangeName = rabbitProperties.getPostRoutingKeyPrefix() + "." + RabbitConstants.EXCHANGE;
    }

    public void publishPostFeedEvent(PostFeedEvent postFeedEvent) {
        String routingKey = rabbitQueueRouterService.getRoutingKey(postFeedEvent.getUserId());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, postFeedEvent);
        log.debug("Published feed event for user {} post {}",
                postFeedEvent.getUserId(), postFeedEvent.getPost().getId());
    }
}
