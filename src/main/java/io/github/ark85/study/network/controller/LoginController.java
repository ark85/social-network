package io.github.ark85.study.network.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/login")
    String login() {
        return "login";
    }
}
