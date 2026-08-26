package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.configuration.ImportProperties;
import io.github.ark85.study.socialnetwork.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ImportProperties importProperties;
}
