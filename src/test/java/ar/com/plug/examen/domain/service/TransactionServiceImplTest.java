package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Transaction;
import ar.com.plug.examen.domain.repository.TransactionRepository;
import ar.com.plug.examen.domain.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl; // Implementation of the service

    private TransactionService transactionService; // Interface of the service

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        transactionService = transactionServiceImpl; // Assign the implementation to the interface reference
    }

    @Test
    @DisplayName("Should create a new transaction successfully")
    void testCreateTransaction() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        // When
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Then
        assertThat(createdTransaction).isNotNull();
        assertThat(createdTransaction.getId()).isEqualTo(1L);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    @DisplayName("Should throw exception when creating transaction fails")
    void testCreateTransactionThrowsException() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        // When
        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException("Error creating transaction"));
        Exception exception = assertThrows(RuntimeException.class, () -> transactionService.createTransaction(transaction));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error creating transaction");
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    @DisplayName("Should return all transactions")
    void testGetAllTransactions() {
        // Given
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        // When
        when(transactionRepository.findAll()).thenReturn(transactions);
        List<Transaction> result = transactionService.getAllTransactions();

        // Then
        assertThat(result).hasSize(2);
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when retrieving all transactions fails")
    void testGetAllTransactionsThrowsException() {
        // When
        when(transactionRepository.findAll()).thenThrow(new RuntimeException("Error retrieving transactions"));
        Exception exception = assertThrows(RuntimeException.class, () -> transactionService.getAllTransactions());

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving transactions");
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return transaction by ID")
    void testGetTransactionById() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        // When
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        Optional<Transaction> result = transactionService.getTransactionById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retrieving transaction by ID fails")
    void testGetTransactionByIdThrowsException() {
        // When
        when(transactionRepository.findById(1L)).thenThrow(new RuntimeException("Error retrieving transaction by id"));
        Exception exception = assertThrows(RuntimeException.class, () -> transactionService.getTransactionById(1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving transaction by id");
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should approve transaction successfully")
    void testApproveTransaction() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setApproved(false);

        // When
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction approvedTransaction = transactionService.approveTransaction(1L);

        // Then
        assertThat(approvedTransaction).isNotNull();
        assertThat(approvedTransaction.getApproved()).isTrue();
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    @DisplayName("Should throw exception when approving transaction fails")
    void testApproveTransactionThrowsException() {
        // When
        when(transactionRepository.findById(1L)).thenThrow(new RuntimeException("Error approving transaction"));
        Exception exception = assertThrows(RuntimeException.class, () -> transactionService.approveTransaction(1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error approving transaction");
        verify(transactionRepository, times(1)).findById(1L);
    }
}
