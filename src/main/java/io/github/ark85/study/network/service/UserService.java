package io.github.ark85.study.network.service;

import io.github.ark85.study.network.model.User;
import io.github.ark85.study.network.model.api.request.UserCreateRequest;
import io.github.ark85.study.network.model.api.response.UserCreateResponse;
import io.github.ark85.study.network.model.api.response.UserGetResponse;
import io.github.ark85.study.network.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserGetResponse getUserById(UUID id) {
        return new UserGetResponse(userRepository.getUserById(id));
    }

    public UserCreateResponse registerUser(UserCreateRequest userCreateRequest) {
        User user = new User(userCreateRequest);
        return new UserCreateResponse(userRepository.createUser(user));
    }
}
