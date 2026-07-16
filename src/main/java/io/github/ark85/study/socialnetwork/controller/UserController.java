package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.UserCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.UserGetResponse;
import io.github.ark85.study.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<UserGetResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(new UserGetResponse(userService.getUserById(id)));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.registerUser(userCreateRequest));
    }
}
