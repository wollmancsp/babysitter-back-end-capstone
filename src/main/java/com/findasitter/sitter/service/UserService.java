package com.findasitter.sitter.service;

import com.findasitter.sitter.user.User;
import com.findasitter.sitter.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // New login method
    public boolean login(String email, String password) {
        Optional<User> userOptional = repo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return encoder.matches(password, user.getUser_password());
        }
        return false;
    }
}
