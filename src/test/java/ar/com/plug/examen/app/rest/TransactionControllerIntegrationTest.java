package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.model.Seller;
import ar.com.plug.examen.domain.model.Transaction;
import ar.com.plug.examen.domain.repository.ClientRepository;
import ar.com.plug.examen.domain.repository.ProductRepository;
import ar.com.plug.examen.domain.repository.SellerRepository;
import ar.com.plug.examen.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Client client;
    private Product product;
    private Seller seller;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Ensure the test database is clean before each test
        transactionRepository.deleteAll();
        clientRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

        // Create necessary entities for the tests
        client = new Client();
        client.setName("Client");
        client.setEmail("client@example.com");
        client = clientRepository.save(client);

        product = new Product();
        product.setName("Product");
        product.setPrice(100.0);
        product.setStock(50);
        product = productRepository.save(product);

        seller = new Seller();
        seller.setName("Seller");
        seller.setEmail("seller@example.com");
        seller = sellerRepository.save(seller);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        String transactionJson = String.format("{\"product\":{\"id\":%d}, \"client\":{\"id\":%d}, \"seller\":{\"id\":%d}, \"quantity\":1, \"date\":\"2024-06-11T12:00:00\", \"approved\":false}",
                product.getId(), client.getId(), seller.getId());

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.id").value(product.getId()))
                .andExpect(jsonPath("$.client.id").value(client.getId()))
                .andExpect(jsonPath("$.seller.id").value(seller.getId()))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        Transaction transaction1 = new Transaction();
        transaction1.setQuantity(1);
        transaction1.setDate(LocalDateTime.parse("2024-06-11T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        transaction1.setApproved(false);
        transaction1.setClient(client);
        transaction1.setProduct(product);
        transaction1.setSeller(seller);

        Transaction transaction2 = new Transaction();
        transaction2.setQuantity(2);
        transaction2.setDate(LocalDateTime.parse("2024-06-11T13:00:00", DateTimeFormatter.ISO_DATE_TIME));
        transaction2.setApproved(true);
        transaction2.setClient(client);
        transaction2.setProduct(product);
        transaction2.setSeller(seller);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[1].quantity").value(2));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setQuantity(1);
        transaction.setDate(LocalDateTime.parse("2024-06-11T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        transaction.setApproved(false);
        transaction.setClient(client);
        transaction.setProduct(product);
        transaction.setSeller(seller);

        transaction = transactionRepository.save(transaction);

        mockMvc.perform(get("/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.approved").value(false));
    }

    @Test
    public void testApproveTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setQuantity(1);
        transaction.setDate(LocalDateTime.parse("2024-06-11T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        transaction.setApproved(false);
        transaction.setClient(client);
        transaction.setProduct(product);
        transaction.setSeller(seller);

        transaction = transactionRepository.save(transaction);

        mockMvc.perform(post("/transactions/{id}/approve", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }
}