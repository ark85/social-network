package io.github.ark85.study.socialnetwork.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@UtilityClass
public class DialogUtils {
    public static UUID generateDialogId(UUID fromUserId, UUID toUserId) {
        UUID firstUserId = fromUserId.compareTo(toUserId) < 0 ? fromUserId : toUserId;
        UUID secondUserId = fromUserId.compareTo(toUserId) < 0 ? toUserId : fromUserId;
        return UUID.nameUUIDFromBytes((firstUserId.toString() + secondUserId).getBytes(StandardCharsets.UTF_8)
        );
    }
}
