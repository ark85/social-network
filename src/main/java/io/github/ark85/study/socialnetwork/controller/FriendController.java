package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/friend")
@Validated
@AllArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PutMapping(path = "/set/{userId}")
    public ResponseEntity<Void> setFriend(@PathVariable UUID userId, Authentication authentication) {
        friendService.setFriend(UUID.fromString(authentication.getName()), userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/delete/{userId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable UUID userId, Authentication authentication) {
        friendService.deleteFriend(UUID.fromString(authentication.getName()), userId);
        return ResponseEntity.ok().build();
    }
}
