package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import io.github.ark85.study.socialnetwork.model.api.response.UserCreateResponse;
import io.github.ark85.study.socialnetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public @NullMarked UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserById(UUID.fromString(username));
    }
}
