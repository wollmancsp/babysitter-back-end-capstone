package com.findasitter.sitter.transaction;

import com.findasitter.sitter.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
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

    //Modify Status of Current Transaction
    public void modifyTransactionStatus(Integer transactionID, Integer newStatus) {
        if(newStatus == 1) {
            //Accepted
            var updated = jdbcClient.sql("UPDATE job SET job_status = ?, job_accepteddate = ? WHERE job_id = ?").params(List.of(newStatus, LocalDateTime.now(), transactionID)).update();
        }else if(newStatus == 2) {
            //Declined
            var updated = jdbcClient.sql("UPDATE job SET job_status = ? WHERE job_id = ?").params(List.of(newStatus, transactionID)).update();
        }
    }

    public List<Transaction> GetTransactionsByUserID(Integer userID) {
        return jdbcClient.sql("SELECT * FROM job WHERE job_parent = :userID OR job_sitter = :userID").param("userID", userID).query(Transaction.class).list();
    }
}