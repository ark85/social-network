package io.github.ark85.study.socialnetwork.controller;

import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.UserCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.UserGetResponse;
import io.github.ark85.study.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
@Validated
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

    @GetMapping(path = "/search")
    public ResponseEntity<List<UserGetResponse>> searchUsers(@RequestParam("first_name") @NotBlank String firstName,
                                                             @RequestParam("second_name") @NotBlank String secondName) {
        return ResponseEntity.ok(userService.searchUsersByFirstNameAndSecondName(firstName, secondName));
    }

    @PostMapping(path = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importUsers(@RequestParam MultipartFile usersFile) throws IOException {
        userService.importUsers(usersFile);
        return ResponseEntity.ok().build();
    }
}
