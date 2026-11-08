package io.github.ark85.study.socialnetwork.client;

import io.github.ark85.study.socialnetwork.model.api.request.DialogMessageSendRequest;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageGetResponse;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageSendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "dialog-client", url = "${social-network.dialogs.url}")
public interface DialogClient {

    @PostMapping("/dialog/{userId}/send")
    DialogMessageSendResponse sendMessage(
            @PathVariable UUID userId, @RequestBody DialogMessageSendRequest dialogMessageSendRequest,
            @RequestHeader("Authorization") String authorization);

    @GetMapping("/dialog/{userId}/list")
    List<DialogMessageGetResponse> getMessages(
            @PathVariable UUID userId, @RequestHeader("Authorization") String authorization);
}
