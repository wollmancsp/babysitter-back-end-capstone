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
        return jdbcClient.sql("select * from user").query(User.class).list();
    }

    public Optional<User> findByEmail(String emailAddress) {
        return jdbcClient.sql("SELECT * FROM User WHERE user_emailaddress = :user_emailaddress").param("user_emailaddress", emailAddress).query(User.class).optional();
    }

    public void create(User user) {
        var updated = jdbcClient.sql("INSERT INTO user(user_emailaddress,user_phone,user_fname,user_lname,user_address,user_city,user_zip) values(?,?,?,?,?,?,?)")
                .params(List.of(user.user_emailaddress(),user.user_phone(),user.user_fname(),user.user_lname(),user.user_address(),user.user_city(),user.user_zip()))
                .update();

        Assert.state(updated == 1, "Failed to create user: " + user.user_emailaddress());
    }

    public void update(User user, String email) {
        var updated = jdbcClient.sql("update user set user_emailaddress = ?,user_phone = ?,user_fname = ?,user_lname = ?,user_address = ?,user_city = ?,user_zip = ? where user_emailaddress = ?")
                .params(List.of(user.user_emailaddress(),user.user_phone(), user.user_fname(), user.user_lname(),user.user_address(),user.user_city(),user.user_zip(), email))
                .update();

        Assert.state(updated == 1, "Failed to update user: " + user.user_emailaddress());
    }

    public User findByUser(String username) {
            return jdbcClient.sql("SELECT * FROM User WHERE user_emailaddress = :user_emailaddress").param("user_emailaddress", username).query(User.class).one();
    }
}
