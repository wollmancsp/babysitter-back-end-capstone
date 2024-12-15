package com.findasitter.sitter.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.findasitter.sitter.constants.GlobalConstants.FRONT_END_PORT;
import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/users")
@CrossOrigin(FRONT_END_PORT)
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

    //Works
    //Security: "/users/get-image-dynamic-type/**
    @GetMapping("/get-image-dynamic-type/{jpg}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@PathVariable boolean jpg) {
        MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        InputStream in = jpg ?
                getClass().getResourceAsStream("/public/profilePicture/43.jpg") :
                getClass().getResourceAsStream("/public/profilePicture/51.jpg");
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
    //End Of Works

//    //Request Profile Picture
//    @GetMapping("/getPfp/{userID}")
//    public ResponseEntity<InputStreamResource> getPfp(@PathVariable Integer userID) {
//        InputStream in = null;
//        MediaType contentType = null;
//        ArrayList<String> allowedFileTypes = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));
//        for (String allowedFileType : allowedFileTypes) {
//            in = getClass().getResourceAsStream("/public/profilePicture/" + userID + "." + allowedFileType);
//            if (in != null) {
//                contentType = returnMediaType(allowedFileType);
//                break;
//            }
//        }
//        if(in != null && contentType != null) {
//            return ResponseEntity.ok()
//                    .contentType(contentType)
//                    .body(new InputStreamResource(in));
//        }else {
//            return null;
//        }
//    }
    ///users/getPfp/**

    //Request Profile Picture
    @GetMapping("/getPfp")
    public ResponseEntity<InputStreamResource> getPfp(@RequestParam("param1") String userID,
                                                      @RequestParam("param2") BigInteger DateTimeUpdated) throws FileNotFoundException {
        //userRepository.fixPFP();

        InputStream in = null;
        MediaType contentType = null;
        ArrayList<String> allowedFileTypes = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));
        for (String allowedFileType : allowedFileTypes) {
            //in = getClass().getResourceAsStream("/public/profilePicture/" + userID + "." + allowedFileType);
            in = new FileInputStream("C:\\Users\\Phillip\\Downloads\\babysitter-back-end-capstone\\target\\" + userID + "." + allowedFileType);
            //System.out.println("Test: " + Paths.get("").toAbsolutePath().toString());
            //C:\Users\Phillip\Downloads\babysitter-back-end-capstone

            System.out.println("IN: " + in.toString());
            if (in != null) {
                contentType = returnMediaType(allowedFileType);
                break;
            }
        }
        if(in != null && contentType != null) {
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(new InputStreamResource(in));
        }else {
            return null;
        }
    }

//    //Request Profile Picture
//    @GetMapping("/getPfp")
//    public ResponseEntity<InputStreamResource> getPfp(@RequestParam("param1") String userID,
//                                                      @RequestParam("param2") BigInteger DateTimeUpdated) {
//        //userRepository.fixPFP();
//
//        InputStream in = null;
//        MediaType contentType = null;
//
//        byte[] byteArray = userRepository.newGetPFP(parseInt(userID));
//        Resource resource = new ByteArrayResource(byteArray);
//        //in = getClass().getResourceAsStream(String.valueOf(resource));
//        in = new ByteArrayInputStream(byteArray);
//        if(resource != null) {
//            //contentType = MediaType.parseMediaType(resource.getFilename());
//            contentType = MediaType.IMAGE_JPEG;
//        }
//        if(in != null && contentType != null) {
//            return ResponseEntity.ok()
//                    .contentType(contentType)
//                    .body(new InputStreamResource(in));
//        }else {
//            System.out.println("Resource Error : " + in);
//            return null;
//        }
//    }

    public MediaType returnMediaType(String stringType) {
        if(Objects.equals(stringType, "jpg") || Objects.equals(stringType, "jpeg"))
            return MediaType.IMAGE_JPEG;
        else if(Objects.equals(stringType, "png"))
            return MediaType.IMAGE_PNG;
        else
            return null;
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

    @PutMapping("/demoteAdmin")
    public ResponseEntity<String> demoteAdmin(@RequestBody DemoteAdminRequest request) {
        try {
            userRepository.demoteAdmin(request.getUser_emailaddress());
            return ResponseEntity.ok("User with email " + request.getUser_emailaddress() + " is no longer admin.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to make user non-admin: " + request.getUser_emailaddress());
        }
    }

    // Creates new user
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/CreateUser")
    void create(@Valid @RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getUser_password());
        user.setUser_password(hashedPassword);
        userRepository.create(user);
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
    @PostMapping("/setUserPFP")
    boolean SetPFP(@RequestBody @RequestParam("image") MultipartFile file, @RequestParam("p1") Integer userID) {
        userRepository.fixPFP(file, userID);
        return userRepository.setUserPFP(file, userID);
    }

    //Toggles the User's enable variable
    @PostMapping("/EditUserProfile")
    boolean SetPFP(@RequestBody User user) {
        userRepository.nonBlankUpdate(user);
        return true;
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

    @PutMapping("/changePassword")
    public ResponseEntity<String> updatePassword(@RequestBody @RequestParam("email") String email, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
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
