package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.model.api.response.PostGetResponse;
import io.github.ark85.study.socialnetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/post")
@Validated
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/feed")
    public ResponseEntity<List<PostGetResponse>> feedPosts(@RequestParam("offset") Integer offset,
                                                           @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(postService.getPosts(offset, limit));
    }

    @PostMapping(path = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importPosts(@RequestParam(name = "file") MultipartFile postsFile) throws IOException {
        postService.importPosts(postsFile);
        return ResponseEntity.ok().build();
    }
}
