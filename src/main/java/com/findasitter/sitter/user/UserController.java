package com.findasitter.sitter.user;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.lang.Integer.parseInt;

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

    // Searches database to find user record with a specified city
    @GetMapping("SearchByCity/{city}")
    List<User> SearchByCity(@PathVariable String city) {
        return userRepository.findByCity(city);
    }

    // Lists all user accounts in database
    @GetMapping("")
    List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    // Searches database to find user record with a specified email address
    @GetMapping("{emailAddress}")
    User findById(@PathVariable String emailAddress) {
        Optional<User> userList = userRepository.findByEmail(emailAddress);
        if (userList.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userList.get();
    }

    // Searches database to find user record with a specified email address
    @GetMapping("FindByUserID/{userID}")
    User findByUserId(@PathVariable Integer userID) {
        Optional<User> run = userRepository.findByUserID(userID);
        if (run.isEmpty()) {
            throw new UserNotFoundException();
        }
        return run.get();
    }

    @PostMapping("/DeleteUser")
    boolean DeleteUser(@RequestBody @RequestParam("p1") String userID) {
        return userRepository.DeleteUser(parseInt(userID));
    }

    /* MERGE */
    @PostMapping("/PromoteUser")
    boolean PromoteToAdmin(@RequestBody @RequestParam("p1") String userID) {
        return userRepository.PromoteToAdmin(parseInt(userID));
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
    /* MERGE */

    /* ADD */
    @PutMapping("/demoteAdmin")
    public ResponseEntity<String> demoteAdmin(@RequestBody DemoteAdminRequest request) {
        try {
            userRepository.demoteAdmin(request.getUser_emailaddress());
            return ResponseEntity.ok("User with email " + request.getUser_emailaddress() + " is no longer admin.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to make user non-admin: " + request.getUser_emailaddress());
        }
    }

    @PostMapping("/create")
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

    // Disable user account in db
//    @PutMapping("/enabledisable")
//    public void enableDisable(@RequestBody DemoteAdminRequest request) {
//        try {
//            userRepository.demoteAdmin(request.getUser_emailaddress());
//            return ResponseEntity.ok("User with email " + request.getUser_emailaddress() + " is no longer admin.");
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to make user non-admin: " + request.getUser_emailaddress());
//        }
//    }
    @GetMapping("/enableUser/{emailAddress}")
    ResponseEntity<String> enableUser(@PathVariable String emailAddress) {
        Optional<User> userList = userRepository.findByEmail(emailAddress);
        try {
            userRepository.enableUser(emailAddress);
            return ResponseEntity.ok(emailAddress + " has been enabled.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to enable: " + emailAddress);
        }
    }
    @GetMapping("/disableUser/{emailAddress}")
    ResponseEntity<String> disableUser(@PathVariable String emailAddress) {
        Optional<User> userList = userRepository.findByEmail(emailAddress);
        try {
            userRepository.disableUser(emailAddress);
            return ResponseEntity.ok(emailAddress + " has been disabled.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to disable: " + emailAddress);
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String currentPassword, @RequestParam String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getUser_password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect.");
        }

        // Update to new password
        userRepository.changePassword(email, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
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
