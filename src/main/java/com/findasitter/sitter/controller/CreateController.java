package com.findasitter.sitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateController {

    @GetMapping("/create")
    public String showCreateForm() {
        return "create"; // form to create a new user
    }
}
