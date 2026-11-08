package io.github.ark85.study.socialnetwork.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogMessageGetResponse {
    private UUID id;
    private UUID dialogId;
    private UUID fromUserId;
    private UUID toUserId;
    private String text;
    private LocalDateTime creationDateTime;
}
