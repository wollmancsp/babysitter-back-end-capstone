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

    // Lists all users in the database
    public List<User> findAllUsers() {
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list(); // Changed to correct table name
    }

    // Finds a user by email
    public Optional<User> findByEmail(String emailAddress) {
        return jdbcClient.sql("SELECT * FROM users WHERE email = :email") // Changed to correct column and table name
                .param("email", emailAddress) // Correctly parametrize the email
                .query(User.class)
                .optional();
    }

    // Creates a new user
    public void create(User user) {
        var updated = jdbcClient.sql("INSERT INTO users(email, user_phone, user_fname, user_lname, user_address, user_city, user_zip) VALUES(?, ?, ?, ?, ?, ?, ?)") // Updated SQL statement
                .params(user.getEmail(), user.getUser_phone(), user.getUser_fname(), user.getUser_lname(), user.getUser_address(), user.getUser_city(), user.getUser_zip()) // Pass params directly
                .update();

        Assert.state(updated == 1, "Failed to create user: " + user.getEmail()); // Assert for successful creation
    }

    // Updates an existing user
    public void update(User user, String email) {
        var updated = jdbcClient.sql("UPDATE users SET email = ?, user_phone = ?, user_fname = ?, user_lname = ?, user_address = ?, user_city = ?, user_zip = ? WHERE email = ?") // Updated SQL statement
                .params(user.getEmail(), user.getUser_phone(), user.getUser_fname(), user.getUser_lname(), user.getUser_address(), user.getUser_city(), user.getUser_zip(), email) // Pass params directly
                .update();

        Assert.state(updated == 1, "Failed to update user: " + user.getEmail()); // Assert for successful update
    }

    // Finds a user by username (email in this case)
    public Optional<User> findByUsername(String username) {
        return jdbcClient.sql("SELECT * FROM users WHERE email = ?") // Changed to correct table name
                .param(username) // Correctly parametrize the username
                .query(User.class)
                .optional();
    }
}
