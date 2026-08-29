package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.configuration.ImportProperties;
import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.api.response.PostGetResponse;
import io.github.ark85.study.socialnetwork.repository.PostRepository;
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
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ImportProperties importProperties;

    @Transactional(readOnly = true)
    public List<PostGetResponse> getPosts(Integer offset, Integer limit) {
        List<Post> posts = postRepository.getPosts(
                !Objects.isNull(offset) && offset >= 0 ? offset : 0,
                !Objects.isNull(limit) && limit > 0 ? limit : 10);
        return posts.stream().map(PostGetResponse::new).toList();
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
