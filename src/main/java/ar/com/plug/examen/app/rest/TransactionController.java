package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Transaction;
import ar.com.plug.examen.domain.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        logger.info("Received request to create transaction: {}", transaction);
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            logger.info("Transaction created successfully: {}", createdTransaction);
            return ResponseEntity.ok(createdTransaction);
        } catch (Exception e) {
            logger.error("Error creating transaction: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        logger.info("Received request to get all transactions");
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            logger.info("Retrieved {} transactions", transactions.size());
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error retrieving transactions: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        logger.info("Received request to get transaction by id: {}", id);
        try {
            return transactionService.getTransactionById(id)
                    .map(transaction -> {
                        logger.info("Transaction retrieved successfully: {}", transaction);
                        return ResponseEntity.ok(transaction);
                    })
                    .orElseGet(() -> {
                        logger.warn("Transaction with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error retrieving transaction by id: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Transaction> approveTransaction(@PathVariable Long id) {
        logger.info("Received request to approve transaction with id: {}", id);
        try {
            Transaction approvedTransaction = transactionService.approveTransaction(id);
            logger.info("Transaction approved successfully: {}", approvedTransaction);
            return ResponseEntity.ok(approvedTransaction);
        } catch (Exception e) {
            logger.error("Error approving transaction: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
