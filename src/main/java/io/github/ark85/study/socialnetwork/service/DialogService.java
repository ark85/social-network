package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.model.DialogMessage;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageGetResponse;
import io.github.ark85.study.socialnetwork.model.api.response.DialogMessageSendResponse;
import io.github.ark85.study.socialnetwork.repository.DialogRepository;
import io.github.ark85.study.socialnetwork.utils.DialogUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class DialogService {

    private final UserService userService;
    private final DialogRepository dialogRepository;

    public DialogMessageSendResponse sendMessage(UUID fromUserId, UUID toUserId, String text) {
        userService.getUserById(toUserId);
        UUID dialogId = DialogUtils.generateDialogId(fromUserId, toUserId);
        DialogMessage dialogMessage = new DialogMessage(
                null, dialogId, fromUserId, toUserId, text, LocalDateTime.now());
        return new DialogMessageSendResponse(dialogRepository.createDialogMessage(dialogMessage));
    }

    @Transactional(transactionManager = "citusTransactionManager", readOnly = true)
    public List<DialogMessageGetResponse> getDialogMessages(UUID fromUserId, UUID toUserId) {
        UUID dialogId = DialogUtils.generateDialogId(fromUserId, toUserId);
        return dialogRepository.getDialogMessages(dialogId).stream().map(DialogMessageGetResponse::new).toList();
    }
}
