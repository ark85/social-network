package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.repository.FriendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public List<UUID> getFriendIds(UUID userId) {
        List<UUID> friends = friendRepository.getFriendIds(userId);
        if (CollectionUtils.isEmpty(friends)) {
            return Collections.emptyList();
        }
        return friends;
    }

    @Transactional
    public void setFriend(UUID userId, UUID friendId) {
        friendRepository.setFriend(userId, friendId);
    }

    @Transactional
    public void deleteFriend(UUID userId, UUID friendId) {
        friendRepository.deleteFriend(userId, friendId);
    }
}
