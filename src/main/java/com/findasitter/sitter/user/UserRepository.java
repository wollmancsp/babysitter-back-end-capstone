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
}
