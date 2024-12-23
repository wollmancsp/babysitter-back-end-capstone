package com.findasitter.sitter.transaction;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.findasitter.sitter.constants.GlobalConstants.FRONT_END_PORT;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(FRONT_END_PORT)
public class TransactionController {
    private final TransactionRepository transactionRepository;
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create New Transaction
    @PostMapping("/TransactionCreate")
    void transactionCreate(@RequestBody @RequestParam("p0") Double parentID, @RequestParam("p1") Double sitterID, @RequestParam("p2") String details, @RequestParam("p3") String startDate, @RequestParam("p4") String endDate, @RequestParam("p5") Double pay) {
        transactionRepository.createTransaction(parentID, sitterID, details, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), pay);
    }

    // Modify Status of Current Transaction
    @PostMapping("/UpdateTransactionStatus")
    Boolean UpdateTransactionStatus(@RequestBody @RequestParam("p0") Integer transactionID, @RequestParam("p1") Integer newStatus) {
        transactionRepository.modifyTransactionStatus(transactionID, newStatus);
        return true;
    }

    //Returns Chat based on requested Chat ID
    @GetMapping("/GetTransactionsByUserID/{userID}")
    List<Transaction> GetTransactionsByUserID(@PathVariable Integer userID) {
        return transactionRepository.GetTransactionsByUserID(userID);
    }

}