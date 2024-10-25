package com.findasitter.sitter.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}