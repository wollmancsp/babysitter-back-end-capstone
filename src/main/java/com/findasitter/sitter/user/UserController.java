package com.findasitter.sitter.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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

// What's below could replace what's above?? Test it out.

//    @GetMapping("{emailAddress}")
//    public User findByEmail(@PathVariable String emailAddress) {
//        return userRepository.findByEmail(emailAddress)
//                .orElseThrow(UserNotFoundException::new);
//    }

    // Creates a new user with an encrypted password
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void create(@Valid @RequestBody User user) {
        // Encrypt the user's password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Set encoded password
        userRepository.create(user);
    }

    // Updates existing user with the specified email address
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{emailAddress}")
    void update(@Valid @RequestBody User user, @PathVariable String emailAddress) {
        // Fetch the existing user based on email and throw NoSuchElementException if not found
        User existingUser = userRepository.findByEmail(emailAddress)
                .orElseThrow(() -> new NoSuchElementException("No user found with email: " + emailAddress));
        // Only re-encode if the password is new/changed
        if (!passwordEncoder.matches(user.getPassword(), userRepository.findByEmail(emailAddress).get().getPassword())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword); // Set encoded password
        }
        userRepository.update(user, emailAddress);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("User not found");
        }
    }
}