package io.github.ark85.study.socialnetwork.service.cache;

import io.github.ark85.study.socialnetwork.configuration.CacheProperties;
import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.cache.PostEvent;
import io.github.ark85.study.socialnetwork.model.cache.PostEventType;
import io.github.ark85.study.socialnetwork.utils.RedisStreamsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

@Service
@Validated
@Slf4j
public class PostCacheService {
    private final RedisTemplate<String, Post> redisTemplate;
    private final String postsCacheKey;
    private final Duration postsCacheTimeToLive;

    public PostCacheService(CacheProperties cacheProperties, RedisTemplate<String, Post> redisTemplate) {
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

    public void rebuildCache(List<Post> posts) {
        redisTemplate.delete(postsCacheKey);
        redisTemplate.opsForList().rightPushAll(postsCacheKey, posts);
        redisTemplate.expire(postsCacheKey, postsCacheTimeToLive);
        log.debug("Rebuilt cache key={} size={}", postsCacheKey, posts.size());
    }

    public void addPostIntoCache(Post post) {
        redisTemplate.opsForList().leftPush(postsCacheKey, post);
        redisTemplate.opsForList().trim(postsCacheKey, 0, 999);
        redisTemplate.expire(postsCacheKey, postsCacheTimeToLive);
        log.debug("Added post {} to cache key={}", post.getId(), postsCacheKey);
    }

    public List<Post> getCachedPosts(int offset, int limit) {
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(postsCacheKey))) {
            return null;
        }
        return redisTemplate.opsForList().range(postsCacheKey, offset, offset + limit - 1);
    }

    public void invalidateCache() {
        redisTemplate.delete(postsCacheKey);
        log.debug("Invalidated cache key={}", postsCacheKey);
    }
}
