package com.findasitter.sitter.user;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
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
    // Creates new user
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void create(@Valid @RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getUser_password());
        user.setUser_password(hashedPassword);
        System.out.println("Encrypted Password: " + hashedPassword);
        userRepository.create(user);
    }

    // Updates existing user with specified email address
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{email}")
    void update(@Valid @RequestBody User user, @PathVariable String email) {
        userRepository.update(user, email);
    }

<<<<<<< Updated upstream

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
=======
    //Find user by user_id
    @GetMapping("GetUserIDs/{userIDList}")
    List<User> findAllUserIDs(@PathVariable Integer[] userIDList) {
        List<User> usersList = new ArrayList<>();
        for (Integer userID : userIDList) {
            Optional<User> tempUser = userRepository.findByUserID(userID);
            tempUser.ifPresent(usersList::add);
        }
        if (usersList.isEmpty()) {
            throw new UserNotFoundException();
        }
        return usersList;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) { //ResponseEntity<String>
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
//        System.out.println("Email: " + loginRequest.getEmail() + " Password: " + loginRequest.getPassword());

        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            System.out.println("Invalid email or password 1");
            return null;
>>>>>>> Stashed changes
        }

        User user = userOptional.get();

        // check that the password matches
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getUser_password())) {
<<<<<<< Updated upstream
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
=======
//            return ResponseEntity.ok("Login successful");
            System.out.println("Login successful");
            return user;
        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            System.out.println("Invalid email or password 2");
            return null;
>>>>>>> Stashed changes
        }
    }
}
