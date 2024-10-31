package com.findasitter.sitter.user;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.findasitter.sitter.config.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
// @CrossOrigin("http://localhost:8080")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lists all user accounts in database
    @GetMapping("")
    List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    // Searches database to find user record with a specified email address
    @GetMapping("{emailAddress}")
    User findById(@PathVariable String emailAddress) {
        Optional<User> run = userRepository.findByEmail(emailAddress);
        if (run.isEmpty()) {
            throw new UserNotFoundException();
        }
        return run.get();
    }

    @PutMapping("/makeAdmin")
    public ResponseEntity<String> makeAdmin(@RequestBody MakeAdminRequest request) {
        try {
            userRepository.makeAdmin(request.getUser_emailaddress());
            return ResponseEntity.ok("User with email " + request.getUser_emailaddress() + " has been made an admin.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to make user admin: " + request.getUser_emailaddress());
        }
    }
    // Creates new user
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/create")
//    void create(@Valid @RequestBody User user) {
//        String hashedPassword = passwordEncoder.encode(user.getUser_password());
//        user.setUser_password(hashedPassword);
//        System.out.println("Encrypted Password: " + hashedPassword);
//        userRepository.create(user);
//    }

    @PostMapping("/create")
    public RedirectView create(@Valid User user) {
        String hashedPassword = passwordEncoder.encode(user.getUser_password());
        user.setUser_password(hashedPassword);
        userRepository.create(user);
        return new RedirectView("/");
    }


    // Updates existing user with specified email address
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{email}")
    void update(@Valid @RequestBody User user, @PathVariable String email) {
        userRepository.update(user, email);
    }


    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) { //ResponseEntity<String>
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("Invalid email or password 1");
            return null;
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getUser_password())) {
            System.out.println("Login successful");
            return user;
        } else {
            System.out.println("Invalid email or password 2");
            return null;
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//        }
//
//        User user = userOptional.get();
//        if (passwordEncoder.matches(loginRequest.getPassword(), user.getUser_password())) {
//            String token = JwtUtil.generateToken(user.getUser_password());
//            return ResponseEntity.ok(token);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//        }
//    }
}
