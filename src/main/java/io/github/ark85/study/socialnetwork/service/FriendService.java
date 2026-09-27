package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.repository.FriendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public List<User> getFriends(UUID userId) {
        return friendRepository.getFriends(userId);
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
