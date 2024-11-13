package com.findasitter.sitter.user;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;


    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }






    public List<User> findAllUsers() {
        return jdbcClient.sql("select * from user")
                .query(User.class)
                .list();
    }

    public List<User> findByCity(String city) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_city = ?")
                .param(city)
                .query(User.class).list();
    }

    public Optional<User> findByEmail(String emailaddress) {
        return jdbcClient.sql("SELECT * FROM user WHERE user_emailaddress = ?")
                .param(emailaddress)
                .query(User.class).optional();
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
//                        user.getUser_address(),
//                        user.getUser_city(),
//                        user.getUser_zip(),
                        user.getUser_password(),
                        user.getUser_role(),
                        user.getUser_enabled()))
                .update();
        Assert.state(updated == 1, "Failed to create user: " + user.getUser_emailaddress());
    }


    public Optional<User> findByUserID(Integer userID) {
        return jdbcClient.sql("SELECT * FROM User WHERE user_ID = :userID").param("userID", userID).query(User.class).optional();
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

}
