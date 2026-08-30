package io.github.ark85.study.socialnetwork.exception;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(UUID id) {
        super("Post is not found: " + id);
    }
}
