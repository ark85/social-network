package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/cache")
@Validated
@AllArgsConstructor
public class CacheController {

    private final PostService postService;

    @PostMapping(path = "/posts/rebuild")
    public ResponseEntity<Void> postsRebuild(Authentication authentication) {
        postService.rebuildCache(UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().build();
    }
}
