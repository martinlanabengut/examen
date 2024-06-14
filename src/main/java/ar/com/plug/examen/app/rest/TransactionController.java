package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Transaction;
import ar.com.plug.examen.domain.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
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

    /**
     * Create a new transaction.
     *
     * @param transaction the transaction to create
     * @return the created transaction
     */
    @ApiOperation(value = "Create a new transaction", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created transaction"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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

    /**
     * Get all transactions.
     *
     * @return the list of all transactions
     */
    @ApiOperation(value = "View a list of available transactions", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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

    /**
     * Get transaction by ID.
     *
     * @param id the ID of the transaction to retrieve
     * @return the transaction with the specified ID
     */
    @ApiOperation(value = "Get a transaction by Id", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved transaction"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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

    /**
     * Approve a transaction.
     *
     * @param id the ID of the transaction to approve
     * @return the updated transaction with approval status
     */
    @ApiOperation(value = "Approve a transaction", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully approved transaction"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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
