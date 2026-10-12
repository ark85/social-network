package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.model.api.request.PostCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.request.PostUpdateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.PostCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.PostGetResponse;
import io.github.ark85.study.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/post")
@Validated
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/feed")
    public ResponseEntity<List<PostGetResponse>> feedPosts(
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit,
            Authentication authentication) {
        return ResponseEntity.ok(postService.getFriendsPosts(UUID.fromString(authentication.getName()), offset, limit));
    }

    @PostMapping(path = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importPosts(
            @RequestParam(name = "file") MultipartFile postsFile, Authentication authentication) throws IOException {
        postService.importPosts(UUID.fromString(authentication.getName()), postsFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/create")
    public ResponseEntity<PostCreateResponse> createPost(
            @RequestBody @Valid PostCreateRequest postCreateRequest, Authentication authentication) {
        return ResponseEntity.ok(postService.createPost(UUID.fromString(authentication.getName()), postCreateRequest));
    }

    @PutMapping(path = "/update")
    public ResponseEntity<Void> updatePost(@RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        postService.updatePost(postUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
