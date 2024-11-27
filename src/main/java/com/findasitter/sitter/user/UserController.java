package com.findasitter.sitter.user;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private final String pfpDir = "uploads";

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

    // Returns 6 random sitters for the initial FAB popup screen.
    @GetMapping("RandomSearchByCity")
    List<User> RandomSearchByCity() {
        return userRepository.RandomSearchByCity();
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
    public RedirectView create(@Valid User user) {
        String hashedPassword = passwordEncoder.encode(user.getUser_password());
        user.setUser_password(hashedPassword);
        userRepository.create(user);
        return new RedirectView("/");
    }

    //For generating test passwords for users.
    @PostMapping("/createTestPasswords")
    public List<String> createTestPasswords(@RequestBody List<String> passwords) {
        List<String> testPasswords = new ArrayList<>();
        for (String password : passwords) {
            testPasswords.add(passwordEncoder.encode(password));
        }
        return testPasswords;
    }

    //Toggles the User's enable variable
    @PostMapping("/ToggleUserEnabled")
    boolean ToggleUserEnabled(@RequestBody @RequestParam("p1") Boolean userEnabled, @RequestParam("p2") Integer userID) {
        return userRepository.ToggleUserEnabled(userEnabled, userID);
    }

    //Toggles the User's enable variable
//    @PostMapping("/setUserPFP")
//    boolean SetPFP(@RequestBody @RequestParam("file") MultipartFile file, @RequestParam("p1") Integer userID) {
//        System.out.println("File: " + file);
//        System.out.println("P1: " + userID);
//        return userRepository.setUserPFP(file, userID);
//    }

    //Toggles the User's enable variable
    @PostMapping("/EditUserProfile")
    boolean SetPFP(@RequestBody User user) { // throws IOException
        //@RequestBody @RequestParam String userID, @RequestParam("image") MultipartFile file, @RequestParam("fileName") String fileName
//        System.out.println("File: " + file);
//        System.out.println("P1: " + userID);

//        String fileLocation = new File("src\\main\\resources\\profilePictures").getAbsolutePath() + "\\" + fileName;
//        System.out.println("Path: " + fileLocation);
//        FileOutputStream fos = new FileOutputStream(fileLocation);
//        fos.write(file.getBytes());
//        fos.close();
//        return userRepository.setUserPFP(file, parseInt(userID));
        System.out.println("Yep");
        userRepository.nonBlankUpdate(user);
        return true;
    }

    //Toggles the User's enable variable
    @Autowired
    ResourceLoader resourceLoader;
    @GetMapping("/ReturnPfp/{fileName}")
    File GetPFP(@PathVariable String fileName) throws IOException {
//        System.out.println("File: " + fileName);

        String fileLocation = new File("src\\main\\resources\\profilePictures").getAbsolutePath() + "\\" + fileName;
//        InputStream fis = new FileInputStream(fileLocation);
        Resource classPathResource = resourceLoader.getResource("classpath:profilePictures/" + fileName);
//        return userRepository.setUserPFP(file, parseInt(userID));
//        if(classPathResource.exists()) {
//            System.out.println("Found!" + classPathResource.getFile().getPath());
//        }

        return classPathResource.getFile();
    }

    // Updates existing user with specified email address
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{email}")
    void update(@Valid @RequestBody User user, @PathVariable String email) {
        userRepository.update(user, email);
    }


    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) { //ResponseEntity<String>
        System.out.println("Email: " + loginRequest.getEmail());
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
}
