package io.github.ark85.study.network.controller;

import io.github.ark85.study.network.model.api.request.UserCreateRequest;
import io.github.ark85.study.network.model.api.response.UserCreateResponse;
import io.github.ark85.study.network.model.api.response.UserGetResponse;
import io.github.ark85.study.network.service.UserService;
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
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.registerUser(userCreateRequest));
    }
}
