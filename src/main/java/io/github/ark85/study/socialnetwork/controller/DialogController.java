package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.model.api.request.DialogMessageSendRequest;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageGetResponse;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageSendResponse;
import io.github.ark85.study.socialnetwork.service.DialogService;
import io.github.ark85.study.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/dialog")
@Validated
@AllArgsConstructor
public class DialogController {

    private final DialogService dialogService;
    private final UserService userService;

    @PostMapping(path = "/{userId}/send")
    public ResponseEntity<DialogMessageSendResponse> sendMessage(
            @PathVariable UUID userId, @RequestBody @Valid DialogMessageSendRequest dialogMessageSendRequest,
            Authentication authentication) {
        // checks if toUser exists
        userService.getUserById(userId);
        return ResponseEntity.ok(dialogService.sendMessage(
                UUID.fromString(authentication.getName()), userId, dialogMessageSendRequest.getText()));
    }

    @GetMapping(path = "/{userId}/list")
    public ResponseEntity<List<DialogMessageGetResponse>> getMessages(
            @PathVariable UUID userId, Authentication authentication) {
        return ResponseEntity.ok(dialogService.getDialogMessages(UUID.fromString(authentication.getName()), userId));
    }
}
