package com.findasitter.sitter.auth;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "Hello, babysitter or parent. Please login!!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Look at you, all logged in and everything. Welcome to the club!";
    }

}
