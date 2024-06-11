package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    Optional<Transaction> getTransactionById(Long id);
    Transaction approveTransaction(Long id);
}
