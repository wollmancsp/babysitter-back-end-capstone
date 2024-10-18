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
        return jdbcClient.sql("select * from user")
                .query(User.class).list();
    }

    public Optional<User> findByEmail(String emailaddress) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = emailaddress")
                .param(List.of(emailaddress))
                .query(User.class).optional();
    }

    public void create(User user) {
        System.out.println("User ID: " + user.user_id());
        System.out.println("User Created: " + user.user_created());
        System.out.println("Email: " + user.user_emailaddress());
        System.out.println("Phone: " + user.user_phone());
        System.out.println("First Name: " + user.user_fname());
        System.out.println("Last Name: " + user.user_lname());
        System.out.println("Address: " + user.user_address());
        System.out.println("City: " + user.user_city());
        System.out.println("ZIP: " + user.user_zip());
        System.out.println("Parent ID: " + user.parent_id());
        System.out.println("Sitter ID: " + user.sitter_id());
        System.out.println("Password: " + user.user_password());
        var updated = jdbcClient.sql("INSERT INTO user(" +
                        "user_id,user_created,user_emailaddress,user_phone,user_fname,user_lname," +
                        "user_address,user_city,user_zip,parent_id,sitter_id,user_password) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?)")
                .params(List.of(
                        user.user_id(),
                        user.user_created(),
                        user.user_emailaddress(),
                        user.user_phone(),
                        user.user_fname(),
                        user.user_lname(),
                        user.user_address(),
                        user.user_city(),
                        user.user_zip(),
                        user.parent_id(),
                        user.sitter_id(),
                        user.user_password()))
                .update();
        Assert.state(updated == 1, "Failed to create user: " + user.user_emailaddress());
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
                        user.user_phone(),
                        user.user_fname(),
                        user.user_lname(),
                        user.user_address(),
                        user.user_city()
//                      user.user_zip(),
//                      user.parent_id(),
//                      user.sitter_id(),
//                      user.user_password()
                         ))
                .update();
        Assert.state(updated == 1, "Failed to update user: " + user.user_emailaddress());
    }
}
