package io.github.ark85.study.socialnetwork.service.queue;

import io.github.ark85.study.socialnetwork.model.cache.PostEventType;
import io.github.ark85.study.socialnetwork.model.queue.PostFeedEvent;
import io.github.ark85.study.socialnetwork.service.cache.PostCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class PostEventListenerService {
    private final PostCacheService postCacheService;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "#{@postFeedQueueNames}")
    public void listenPostFeedEvents(PostFeedEvent postFeedEvent) {
        postCacheService.addPostEventIntoStream(PostEventType.CREATED, postFeedEvent.getPost());
        messagingTemplate.convertAndSendToUser(
                postFeedEvent.getUserId().toString(),
                "/post/feed/posted",
                postFeedEvent.getPost()
        );
    }
}
