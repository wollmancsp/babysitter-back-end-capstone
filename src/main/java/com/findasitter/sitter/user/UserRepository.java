package com.findasitter.sitter.user;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.findasitter.sitter.constants.GlobalConstants.IDE_BASE_PATH;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;

    @Autowired
    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean DeleteUser(Integer userID) {
        var updated = jdbcClient.sql("DELETE FROM user WHERE user_id = ?;").param(userID).update();
        Assert.state(updated == 1, "Failed to update user: " + userID);
        return true;
    }

    public List<User> findByCity(String city) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_city = ?")
                .param(city)
                .query(User.class).list();
    }

    public List<User> RandomSearchByCity() {
        return jdbcClient.sql("SELECT * FROM user ORDER BY RAND() LIMIT 6")
                .query(User.class).list();
    }

    public boolean PromoteToAdmin(Integer userID) {
        var updated = jdbcClient.sql("UPDATE user SET user_role = ? WHERE user_id = ?")
                .params(List.of(
                        1,
                        userID
                ))
                .update();
        Assert.state(updated == 1, "Failed to update user: " + userID);
        return true;
    }

    public Optional<User> findByUserID(Integer userID) {
        return jdbcClient.sql("SELECT * FROM User WHERE user_ID = :userID").param("userID", userID).query(User.class).optional();
    }

    public List<User> findAllUsers() {
        return jdbcClient.sql("select * from user")
                .query(User.class)
                .list();
    }

    public Optional<User> findByEmail(String emailaddress) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = ?")
                .param(emailaddress)
                .query(User.class).optional();
    }

    public void makeAdmin(String emailaddress) {
        var updateRole = jdbcClient.sql("UPDATE user SET user_role = 0 WHERE user_emailaddress = ?")
                .params(emailaddress)
                .update();
        Assert.state(updateRole == 1, "Failed to make user admin: " + emailaddress);
    }

    @Autowired
    ServletContext context;
    @CacheEvict(value ="user" ,allEntries = true)
    public boolean setUserPFP(MultipartFile file, Integer userID) {
        try {
            byte[] imageByteArray = file.getBytes();
            String fileName = userID + "." + file.getOriginalFilename().split("\\.")[1];
            String fileNameWithoutExtension = userID.toString();
            String basePath = IDE_BASE_PATH;
            Path dirPath = Paths.get(basePath);

            if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
                throw new IllegalArgumentException("Invalid directory path: " + basePath);
            }

            // Use DirectoryStream to filter files with the matching name
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, fileNameWithoutExtension + ".*")) {
                for (Path file2 : stream) {
                    //Use these comments to see if some weird delete bug happens.
                    //System.out.println("Poss File: " + file2.getFileName().toString());
                    var ext = file.getOriginalFilename().split("\\.")[1];
                    if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")) {
                        //System.out.println("Deleted: " + file2.getFileName());
                        Files.delete(file2);
                    }
                }
            }

            String fileLocation = basePath + "\\" + fileName;
            FileOutputStream fos = new FileOutputStream(fileLocation);
            fos.write(imageByteArray);
            fos.close();
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
        return true;
    }

    public Boolean ToggleUserEnabled (Boolean userEnabled, Integer userID) {
        if(userEnabled) {
            var updateEnabled = jdbcClient.sql("UPDATE user SET user_enabled = 0 WHERE user_id = ?")
                    .params(userID)
                    .update();
        }else {
            var updateEnabled = jdbcClient.sql("UPDATE user SET user_enabled = 1 WHERE user_id = ?")
                    .params(userID)
                    .update();
        }
        return true;
    }

    public void demoteAdmin (String emailaddress) {
        var updateRoles = jdbcClient.sql("UPDATE user SET user_role = 1 WHERE user_emailaddress = ?")
                .params(emailaddress)
                .update();
        Assert.state(updateRoles == 1, "Failed to make user non-admin: " + emailaddress);
    }

    public void create(User user) {
        var updated = jdbcClient.sql("INSERT INTO user(" +
                        "user_emailaddress,user_phone,user_fname,user_lname," +
                        "user_password,user_role,user_enabled) " +
                        "values(?,?,?,?,?,?,?)")

                .params(List.of(
                        user.getUser_emailaddress(),
                        user.getUser_phone(),
                        user.getUser_fname(),
                        user.getUser_lname(),
                        user.getUser_password(),
                        user.getUser_role(),
                        user.getUser_enabled()))

                .update();
        Assert.state(updated == 1, "Failed to create user: " + user.getUser_emailaddress());
    }

    public void changePassword(String emailaddress, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        var updated = jdbcClient.sql("UPDATE user SET user_password = ? WHERE user_emailaddress = ?")
                .params(encodedPassword, emailaddress)
                .update();

        Assert.state(updated == 1, "Failed to update password for user: " + emailaddress);
    }

    public void update(User user, String email) {
        var updated = jdbcClient.sql("update user set " +
                        "user_emailaddress = ?," +
                        "user_phone = ?," +
                        "user_fname = ?," +
                        "user_lname = ?," +
                        "user_address = ?," +
                        "user_city = ?," +
                        "user_zip = ?" +
                        "user_aboutme = ?" +
                        " where emailaddress = ?")
                .params(List.of(
                        email,
                        user.getUser_phone(),
                        user.getUser_fname(),
                        user.getUser_lname(),
                        user.getUser_address(),
                        user.getUser_city(),
                        user.getUser_zip(),
                        user.getUser_aboutme(),
                        email
                         ))
                .update();
        Assert.state(updated == 1, "Failed to update user: " + user.getUser_emailaddress());
    }

    public void nonBlankUpdate(User user) {
        Optional<User> oldUser = jdbcClient.sql("SELECT * FROM user WHERE user_id = ?")
                .param(user.getUser_id())
                .query(User.class).optional();

        if (oldUser.isPresent()) {
            User newUser = oldUser.get();
            if(user.getUser_phone() != null) {
                newUser.setUser_phone(user.getUser_phone());
            }
            if(user.getUser_fname()  !=  null) {
                newUser.setUser_fname(user.getUser_fname());
            }
            if(user.getUser_lname()  !=  null) {
                newUser.setUser_lname(user.getUser_lname());
            }
            if(user.getUser_address() != null) {
                newUser.setUser_address(user.getUser_address());
            }
            if(user.getUser_city() !=  null) {
                newUser.setUser_city(user.getUser_city());
            }
            if(user.getUser_zip() !=  null) {
                newUser.setUser_zip(user.getUser_zip());
            }
            if(user.getUser_aboutme() !=  null) {
                newUser.setUser_aboutme(user.getUser_aboutme());
            }

            var updated = jdbcClient.sql("update user set " +
                            "user_phone = ?," +
                            "user_fname = ?," +
                            "user_lname = ?," +
                            "user_address = ?," +
                            "user_city = ?," +
                            "user_zip = ?," +
                            "user_aboutme = ?" +
                            " where user_id = ?")
                    .params(List.of(
                            newUser.getUser_phone(),
                            newUser.getUser_fname(),
                            newUser.getUser_lname(),
                            newUser.getUser_address(),
                            newUser.getUser_city(),
                            newUser.getUser_zip(),
                            newUser.getUser_aboutme(),
                            user.getUser_id()
                    ))
                    .update();
        }else {
            System.out.println("Error: User does not exist!");
        }
    }


    public void changePassword(String emailaddress, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        var updated = jdbcClient.sql("UPDATE user SET user_password = ? WHERE user_emailaddress = ?")
                .params(encodedPassword, emailaddress)
                .update();

        Assert.state(updated == 1, "Failed to update password for user: " + emailaddress);
    }
}
