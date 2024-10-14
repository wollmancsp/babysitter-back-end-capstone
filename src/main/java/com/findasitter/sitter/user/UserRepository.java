package com.findasitter.sitter.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<User> findAllUsers() {
        return jdbcClient.sql("SELECT * FROM user").query(User.class).list();
    }
//dustin
//    public Optional<User> findByEmail(String emailAddress) {
//        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = :user_emailaddress")
//                .param(emailAddress)
//                .query(User.class).optional();
//    }

    public Optional<User> findByEmail(String emailAddress) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = :emailAddress")  // Correct column name and table
                .param("emailAdress", emailAddress)
                .query(User.class)
                .optional();
    }

//    public void create(User user) {
//        var updated = jdbcClient.sql("INSERT INTO user(user_emailaddress,user_phone,user_fname,user_lname,user_address,user_city,user_zip) values(?,?,?,?,?,?,?)")
//                .params(List.of(user.user_emailaddress(),user.user_phone(),user.user_fname(),user.user_lname(),user.user_address(),user.user_city(),user.user_zip()))
//                .update();
//
//        Assert.state(updated == 1, "Failed to create user: " + user.user_emailaddress());
//    }

    public void create(User user) {
        var updated = jdbcClient.sql("INSERT INTO user(user_emailaddress, user_phone, user_fname, user_lname, user_address, user_city, user_zip) VALUES(?, ?, ?, ?, ?, ?, ?)")
                .params(user.getEmail(), user.getUser_phone(), user.getUser_fname(), user.getUser_lname(), user.getUser_address(), user.getUser_city(), user.getUser_zip())  // Pass params directly
                .update();
        Assert.state(updated == 1, "Failed to create user: " + user.getEmail());
    }

//    public void update(User user, String email) {
//        var updated = jdbcClient.sql("update user set user_emailaddress = ?,user_phone = ?,user_fname = ?,user_lname = ?,user_address = ?,user_city = ?,user_zip = ? where user_emailaddress = ?")
//                .params(List.of(user.user_emailaddress(),user.user_phone(), user.user_fname(), user.user_lname(),user.user_address(),user.user_city(),user.user_zip(), email))
//                .update();
//
//        Assert.state(updated == 1, "Failed to update user: " + user.user_emailaddress());
//    }

    public void update(User user, String email) {
        var updated = jdbcClient.sql("UPDATE user SET user_emailaddress = ?, user_phone = ?, user_fname = ?, user_lname = ?, user_address = ?, user_city = ?, user_zip = ? WHERE email = ?")
                .params(user.getEmail(), user.getUser_phone(), user.getUser_fname(), user.getUser_lname(), user.getUser_address(), user.getUser_city(), user.getUser_zip(), email)  // Pass params directly
                .update();

        Assert.state(updated == 1, "Failed to update user: " + user.getEmail());
    }

//    public Optional<User> findByUsername(String emailAddress) {
//        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = ?")
//                .param(emailAddress)
//                .query(User.class)
//                .optional();
//    }
}
