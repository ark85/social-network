package io.github.ark85.study.socialnetwork.service.cache;

import io.github.ark85.study.socialnetwork.model.cache.PostEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class PostEventConsumer {
    private final PostCacheService postCacheService;

    public void handlePostEvent(ObjectRecord<String, PostEvent> message) {
        PostEvent postEvent = message.getValue();
        switch (postEvent.getPostEventType()) {
            case CREATED -> postCacheService.addPostIntoCache(postEvent.getPost());
            case UPDATED, DELETED -> postCacheService.invalidateCache();
        }
    }
}
