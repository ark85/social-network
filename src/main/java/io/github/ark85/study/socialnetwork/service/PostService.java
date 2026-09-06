package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.configuration.CacheProperties;
import io.github.ark85.study.socialnetwork.configuration.ImportProperties;
import io.github.ark85.study.socialnetwork.exception.PostNotFoundException;
import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.api.request.PostCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.request.PostUpdateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.PostCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.PostGetResponse;
import io.github.ark85.study.socialnetwork.model.cache.PostEventType;
import io.github.ark85.study.socialnetwork.repository.PostRepository;
import io.github.ark85.study.socialnetwork.service.cache.PostCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class PostService {

    private final PostCacheService postCacheService;
    private final PostRepository postRepository;
    private final ImportProperties importProperties;
    private final CacheProperties cacheProperties;

    @Transactional(readOnly = true)
    public List<PostGetResponse> getPosts(Integer offset, Integer limit) {
        int actualOffset = !Objects.isNull(offset) && offset >= 0 ? offset : 0;
        int actualLimit = !Objects.isNull(limit) && limit > 0 ? limit : 10;
        if (actualOffset + actualLimit <= 1000) {
            List<Post> cachedPosts = postCacheService.getCachedPosts(actualOffset, actualLimit);
            if (cachedPosts != null) {
                return cachedPosts.stream().map(PostGetResponse::new).toList();
            }
            rebuildCache();
            return postCacheService.getCachedPosts(actualOffset, actualLimit).stream()
                    .map(PostGetResponse::new).toList();
        }

        List<Post> posts = postRepository.getPosts(actualOffset, actualLimit);
        return posts.stream().map(PostGetResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public void rebuildCache() {
        List<Post> posts = postRepository.getPosts(cacheProperties.getCacheSize());
        postCacheService.rebuildCache(posts);
    }

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest postCreateRequest) {
        Post post = new Post(null, postCreateRequest.getText(), LocalDateTime.now());
        UUID id = postRepository.createPost(post);
        post.setId(id);
        postCacheService.addPostEventIntoStream(PostEventType.CREATED, post);
        return new PostCreateResponse(id);
    }

    @Transactional
    public void updatePost(PostUpdateRequest postUpdateRequest) {
        int updated = postRepository.updatePost(postUpdateRequest.getId(), postUpdateRequest.getText());
        if (updated == 0) {
            throw new PostNotFoundException(postUpdateRequest.getId());
        }
        postCacheService.addPostEventIntoStream(PostEventType.UPDATED, null);
    }

    @Transactional
    public void deletePost(UUID id) {
        int deleted = postRepository.deletePost(id);
        if (deleted == 0) {
            throw new PostNotFoundException(id);
        }
        postCacheService.addPostEventIntoStream(PostEventType.DELETED, null);
    }

    @Transactional
    public void importPosts(MultipartFile postsFile) throws IOException {
        log.info("Start importPosts with batchSize = {}", importProperties.getBatchSize());
        try (
                Reader reader = new InputStreamReader(postsFile.getInputStream(), StandardCharsets.UTF_8);
        ) {
            List<String> filePosts = reader.readAllLines();
            for (List<String> filePostsPartition : ListUtils.partition(filePosts, importProperties.getBatchSize())) {
                postRepository.createAll(filePostsPartition);
            }
        }
        log.info("importPosts is successfully finished");
    }
}
