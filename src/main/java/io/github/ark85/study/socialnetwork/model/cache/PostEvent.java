package io.github.ark85.study.socialnetwork.model.cache;

import io.github.ark85.study.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostEvent {
    PostEventType postEventType;
    Post post;
}
