package com.findasitter.sitter.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;


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

    public void makeAdmin (String emailaddress) {
        var updateRole = jdbcClient.sql("UPDATE user SET user_role = 0 WHERE user_emailaddress = ?")
                .params(emailaddress)
                .update();
        Assert.state(updateRole == 1, "Failed to make user admin: " + emailaddress);
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
                        "user_password) " +
                        "values(?,?,?,?,?)")
                .params(List.of(
                        user.getUser_emailaddress(),
                        user.getUser_phone(),
                        user.getUser_fname(),
                        user.getUser_lname(),
                        //user.getUser_address(),
                        //user.getUser_city(),
                        //user.getUser_zip(),
                        //user.getParent_id(),
                        //user.getSitter_id(),
                        user.getUser_password()))
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
                        " where emailaddress = ?")
                .params(List.of(
                    //  user.user_id(),
                    //  user.user_created(),
                        email,
                        user.getUser_phone(),
                        user.getUser_fname(),
                        user.getUser_lname(),
                        user.getUser_address(),
                        user.getUser_city()
//                      user.user_zip(),
//                      user.parent_id(),
//                      user.sitter_id(),
//                      user.user_password()
                         ))
                .update();
        Assert.state(updated == 1, "Failed to update user: " + user.getUser_emailaddress());
    }

//    public void save(User user) {
//        var updated = jdbcClient.sql("INSERT INTO user(" +
//                        "user_emailaddress, user_phone, user_fname, user_lname, " +
//                        "user_address, user_city, user_zip, parent_id, sitter_id, user_password) " +
//                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
//                .params(List.of(
//                        user.getUser_emailaddress(),
//                        user.getUser_phone(),
//                        user.getUser_fname(),
//                        user.getUser_lname(),
//                        user.getUser_address(),
//                        user.getUser_city(),
//                        user.getUser_zip(),
//                        user.getParent_id(),
//                        user.getSitter_id(),
//                        user.getUser_password()))
//                .update();
//        Assert.state(updated == 1, "Failed to create user: " + user.getUser_emailaddress());
//    }

    public void enableUser (String emailaddress) {
        var setUserEnabled = jdbcClient.sql("UPDATE user SET user_enabled = 1 WHERE user_emailaddress = ?")
                .params(emailaddress)
                .update();
        Assert.state(setUserEnabled == 1, "Failed to enable user: " + emailaddress);
    }
    public void disableUser (String emailaddress) {
        var setUserEnabled = jdbcClient.sql("UPDATE user SET user_enabled = 0 WHERE user_emailaddress = ?")
                .params(emailaddress)
                .update();
        Assert.state(setUserEnabled == 1, "Failed to disable user: " + emailaddress);
    }
}
