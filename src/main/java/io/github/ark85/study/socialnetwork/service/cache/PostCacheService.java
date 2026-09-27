package io.github.ark85.study.socialnetwork.service.cache;

import io.github.ark85.study.socialnetwork.configuration.CacheProperties;
import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.cache.PostEvent;
import io.github.ark85.study.socialnetwork.model.cache.PostEventType;
import io.github.ark85.study.socialnetwork.service.FriendService;
import io.github.ark85.study.socialnetwork.utils.RedisStreamsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Slf4j
public class PostCacheService {
    private final FriendService friendService;
    private final RedisTemplate<String, Post> redisTemplate;
    private final String postsCacheKey;
    private final Duration postsCacheTimeToLive;

    public PostCacheService(FriendService friendService, RedisTemplate<String, Post> redisTemplate,
                            CacheProperties cacheProperties) {
        this.friendService = friendService;
        this.redisTemplate = redisTemplate;
        this.postsCacheKey = cacheProperties.getPostsCacheKey();
        this.postsCacheTimeToLive = Duration.ofHours(cacheProperties.getPostsCacheTimeToLiveHours());
    }

    public void addPostEventIntoStream(PostEventType postEventType, Post post) {
        var recordId = redisTemplate.opsForStream().add(
                StreamRecords.newRecord()
                        .in(RedisStreamsConstants.POST_EVENTS)
                        .ofObject(new PostEvent(postEventType, post))
        );
        log.debug("Published {} event to stream {} with id {}",
                postEventType, RedisStreamsConstants.POST_EVENTS, recordId);
    }

    public void rebuildCache(UUID userId, List<Post> posts) {
        String cacheKey = postsCacheKey + ":" + userId;
        redisTemplate.delete(cacheKey);
        if (CollectionUtils.isEmpty(posts)) {
            log.debug("Posts are empty. Cleaned up cache by key = {}", cacheKey);
            return;
        }
        redisTemplate.opsForList().rightPushAll(cacheKey, posts);
        redisTemplate.expire(cacheKey, postsCacheTimeToLive);
        log.debug("Rebuilt cache key={} size={}", cacheKey, posts.size());
    }

    public void addPostIntoCache(Post post) {
        friendService.getFriendIds(post.getAuthorId()).forEach(friendId -> {
            String cacheKey = postsCacheKey + ":" + friendId;
            redisTemplate.opsForList().leftPush(cacheKey, post);
            redisTemplate.opsForList().trim(cacheKey, 0, 999);
            redisTemplate.expire(cacheKey, postsCacheTimeToLive);
            log.debug("Added post {} to cache key={}", post.getId(), cacheKey);
        });
    }

    public List<Post> getCachedPosts(UUID userId, int offset, int limit) {
        String cacheKey = postsCacheKey + ":" + userId;
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            return null;
        }
        return redisTemplate.opsForList().range(cacheKey, offset, offset + limit - 1);
    }

    public void invalidateCache(UUID authorId) {
        friendService.getFriendIds(authorId).forEach(friendId -> {
            String cacheKey = postsCacheKey + ":" + friendId;
            redisTemplate.delete(cacheKey);
            log.debug("Invalidated cache key={}", cacheKey);
        });
    }
}
