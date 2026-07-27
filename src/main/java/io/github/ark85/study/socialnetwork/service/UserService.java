package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.UserCreateResponse;
import io.github.ark85.study.socialnetwork.model.api.response.UserGetResponse;
import io.github.ark85.study.socialnetwork.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @NullMarked UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserById(UUID.fromString(username));
    }

    public User getUserById(UUID id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User is not found.");
        }
        return user;
    }

    public UserCreateResponse registerUser(UserCreateRequest userCreateRequest) {
        User user = new User(userCreateRequest);
        String passwordHash = passwordEncoder.encode(userCreateRequest.getPassword());
        user.setPasswordHash(passwordHash);
        return new UserCreateResponse(userRepository.createUser(user));
    }

    public List<UserGetResponse> searchUsersByFirstNameAndSecondName(
            @NotBlank String firstName, @NotBlank String secondName) {
        List<User> users = userRepository.searchUsersByFirstNameAndSecondName(firstName, secondName);
        return users.stream().map(UserGetResponse::new).toList();
    }
}
