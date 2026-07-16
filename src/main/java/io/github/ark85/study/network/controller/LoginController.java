package io.github.ark85.study.network.controller;

import io.github.ark85.study.network.model.api.request.UserLoginRequest;
import io.github.ark85.study.network.model.api.response.UserLoginResponse;
import io.github.ark85.study.network.service.JwtService;
import io.github.ark85.study.network.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getId(),
                        userLoginRequest.getPassword()));

        UserDetails user = userService.loadUserByUsername(userLoginRequest.getId());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new UserLoginResponse(token));
    }
}
