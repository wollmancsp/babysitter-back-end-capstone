package com.findasitter.sitter.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:8080")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // For password encryption

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Inject PasswordEncoder
    }

    // Lists all user accounts in the database
    @GetMapping("")
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    // Searches database to find user record with a specified email address
    @GetMapping("{emailAddress}")
    public User findByEmail(@PathVariable String emailAddress) {
        Optional<User> user = userRepository.findByEmail(emailAddress);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    // Creates a new user with an encrypted password
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@Valid @RequestBody User user) {
        // Encrypt the user's password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Set encoded password

        userRepository.create(user);
    }

    // Updates existing user with the specified email address
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{email}")
    public void update(@Valid @RequestBody User user, @PathVariable String email) {
        // Encrypt the user's password before updating
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Set encoded password

        userRepository.update(user, email);
    }
}



//package com.findasitter.sitter.user;
//
//
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/users")
//@CrossOrigin("http://localhost:8080")
//public class UserController {
//    private final UserRepository userRepository;
//
//    public UserController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // Lists all user accounts in database
//    @GetMapping("")
//    List<User> findAllUsers() {
//        return userRepository.findAllUsers();
//    }
//
//    // Searches database to find user record with a specified email address
//    @GetMapping("{emailAddress}")
//    User findById(@PathVariable String emailAddress) {
//        Optional<User> run = userRepository.findByEmail(emailAddress);
//        if (run.isEmpty()) {
//            throw new UserNotFoundException();
//        }
//        return run.get();
//    }
//    // Creates new user
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    void create(@Valid @RequestBody User user) {
//        userRepository.create(user);
//    }
//
//    // Updates existing user with specified email address
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PutMapping("/{email}")
//    void update(@Valid @RequestBody User user, @PathVariable String email) {
//        userRepository.update(user, email);
//    }
//
//}
