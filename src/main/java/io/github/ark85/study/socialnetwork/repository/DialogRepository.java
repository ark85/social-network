package io.github.ark85.study.socialnetwork.repository;

import io.github.ark85.study.socialnetwork.model.DialogMessage;
import io.tarantool.client.box.TarantoolBoxClient;
import io.tarantool.mapping.TarantoolResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class DialogRepository {

    private final TarantoolBoxClient tarantoolClient;

    public UUID createDialogMessage(DialogMessage dialogMessage) {
        List<Object> messageFields = List.of(
                dialogMessage.getDialogId(),
                dialogMessage.getFromUserId(),
                dialogMessage.getToUserId(),
                dialogMessage.getText(),
                dialogMessage.getCreationDateTime()
        );
        TarantoolResponse<List<UUID>> response = this.tarantoolClient
                .call("dialog_send", messageFields, UUID.class)
                .join();
        List<UUID> messageIds = response.get();
        if (messageIds == null || messageIds.isEmpty() || messageIds.getFirst() == null) {
            throw new IllegalStateException("dialog_send returned no message id");
        }
        return messageIds.getFirst();
    }

    public List<DialogMessage> getDialogMessages(UUID dialogId) {
        TarantoolResponse<List<DialogMessage>> response = this.tarantoolClient
                .call("dialog_list", List.of(dialogId), DialogMessage.class)
                .join();
        List<DialogMessage> messages = response.get();
        if (messages == null) {
            return List.of();
        }
        return messages;
    }
}
