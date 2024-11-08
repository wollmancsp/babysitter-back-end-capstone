package com.findasitter.sitter.transaction;

import com.findasitter.sitter.chat.Chat;
import com.findasitter.sitter.transaction.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/transaction")
@CrossOrigin("http://localhost:4200")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create New Transaction
    @PostMapping("/TransactionCreate")
    void transactionCreate(@RequestBody @RequestParam("p0") Double parentID, @RequestParam("p1") Double sitterID, @RequestParam("p2") String details, @RequestParam("p3") String startDate, @RequestParam("p4") String endDate, @RequestParam("p5") Double pay) {
        //System.out.println("transactionCreate Called");
        transactionRepository.createTransaction(parentID, sitterID, details, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), pay);
    }
}
