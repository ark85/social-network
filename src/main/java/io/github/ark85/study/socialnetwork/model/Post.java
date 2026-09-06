package io.github.ark85.study.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private UUID id;
    private String text;
    private LocalDateTime creationDateTime;
}
