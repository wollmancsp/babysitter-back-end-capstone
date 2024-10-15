package com.findasitter.sitter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to FindASitter!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "This is a secured page!";
    }

}
