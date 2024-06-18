package ar.com.plug.examen.domain.service.impl;

import ar.com.plug.examen.domain.model.Transaction;
import ar.com.plug.examen.domain.repository.TransactionRepository;
import ar.com.plug.examen.domain.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        logger.debug("Creating transaction with details: {}", transaction);
        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            logger.debug("Transaction created successfully: {}", savedTransaction);
            return savedTransaction;
        } catch (Exception e) {
            logger.error("Error creating transaction: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        logger.debug("Retrieving all transactions");
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            logger.debug("Retrieved {} transactions", transactions.size());
            return transactions;
        } catch (Exception e) {
            logger.error("Error retrieving transactions: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        logger.debug("Retrieving transaction by id: {}", id);
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            transaction.ifPresent(t -> logger.debug("Transaction retrieved successfully: {}", t));
            return transaction;
        } catch (Exception e) {
            logger.error("Error retrieving transaction by id: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Transaction approveTransaction(Long id) {
        logger.debug("Approving transaction with id: {}", id);
        try {
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
            transaction.setApproved(true);
            Transaction approvedTransaction = transactionRepository.save(transaction);
            logger.debug("Transaction approved successfully: {}", approvedTransaction);
            return approvedTransaction;
        } catch (Exception e) {
            logger.error("Error approving transaction: {}", e.getMessage());
            throw e;
        }
    }
}
