package io.github.ark85.study.socialnetwork.model.api.response;

import io.github.ark85.study.socialnetwork.model.DialogMessage;
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

    public DialogMessageGetResponse(DialogMessage dialogMessage) {
        this.id = dialogMessage.getId();
        this.fromUserId = dialogMessage.getFromUserId();
        this.toUserId = dialogMessage.getToUserId();
        this.text = dialogMessage.getText();
        this.creationDateTime = dialogMessage.getCreationDateTime();
    }
}
