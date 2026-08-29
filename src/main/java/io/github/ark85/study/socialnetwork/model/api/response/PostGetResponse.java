package io.github.ark85.study.socialnetwork.model.api.response;

import io.github.ark85.study.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGetResponse {
    private UUID id;
    private String text;
    private LocalDateTime creationDateTime;

    public PostGetResponse(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.creationDateTime = post.getCreationDateTime();
    }
}
