package com.findasitter.sitter.transaction;

import com.findasitter.sitter.chat.Message;
import com.findasitter.sitter.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcClient jdbcClient;
    public TransactionRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //Create New Transaction
    public void createTransaction(Double parentID, Double sitterID, String details, LocalDateTime startDate, LocalDateTime endDate, Double pay) {
        var updated = jdbcClient.sql("INSERT INTO job(job_parent, job_sitter, job_details, job_created, job_start, job_end, job_pay, job_status) values(?,?,?,?,?,?,?,?)")
                .params(List.of(parentID, sitterID, details, LocalDateTime.now(), startDate, endDate, pay, 0))
                .update();

        Assert.state(updated == 1, "Failed to create transaction");
    }
}
