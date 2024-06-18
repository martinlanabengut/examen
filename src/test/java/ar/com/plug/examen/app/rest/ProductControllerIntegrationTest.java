package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.repository.ClientRepository;
import ar.com.plug.examen.domain.repository.ProductRepository;
import ar.com.plug.examen.domain.repository.SellerRepository;
import ar.com.plug.examen.domain.repository.TransactionRepository;
import ar.com.plug.examen.domain.service.ProductService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private ProductService productService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        transactionRepository.deleteAll();
        clientRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

    }

    @Test
    public void testCreateProduct() throws Exception {
        String productJson = "{\"name\":\"Product 1\", \"price\":10.0, \"stock\":100}";

        Product product = new Product();
        product.setName("Product 1");
        product.setPrice(10.0);
        product.setStock(100);

        Mockito.when(productService.createProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    public void testCreateProductWithInvalidData() throws Exception {
        String invalidProductJson = "{\"name\":\"\", \"price\":10.0, \"stock\":100}";

        Mockito.when(productService.createProduct(Mockito.any(Product.class)))
                .thenThrow(new IllegalArgumentException("Invalid product data"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(10.0);
        product1.setStock(100);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(20.0);
        product2.setStock(200);

        productRepository.save(product1);
        productRepository.save(product2);

        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].stock").value(100))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[1].price").value(20.0))
                .andExpect(jsonPath("$[1].stock").value(200));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setName("Product");
        product.setPrice(10.0);
        product.setStock(100);

        product = productRepository.save(product);

        Mockito.when(productService.getProductById(product.getId())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        Long productId = 1L;

        Mockito.when(productService.getProductById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Product");
        product.setPrice(10.0);
        product.setStock(100);

        product = productRepository.save(product);

        String updatedProductJson = "{\"name\":\"Updated Product\", \"price\":20.0, \"stock\":200}";

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(20.0);
        updatedProduct.setStock(200);

        Mockito.when(productService.updateProduct(Mockito.eq(product.getId()), Mockito.any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.stock").value(200));
    }

    @Test
    public void testUpdateProductNotFound() throws Exception {
        Long productId = 999L;
        String updatedProductJson = "{\"name\":\"Updated Product\", \"price\":20.0, \"stock\":200}";

        Mockito.when(productService.updateProduct(Mockito.eq(productId), Mockito.any(Product.class)))
                .thenThrow(new NoSuchElementException("Product not found"));

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProductWithInvalidData() throws Exception {
        Product product = new Product();
        product.setName("Product");
        product.setPrice(10.0);
        product.setStock(100);

        product = productRepository.save(product);

        String invalidUpdatedProductJson = "{\"name\":\"\", \"price\":20.0, \"stock\":200}";

        Mockito.when(productService.updateProduct(Mockito.eq(product.getId()), Mockito.any(Product.class)))
                .thenThrow(new IllegalArgumentException("Invalid product data"));

        mockMvc.perform(put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdatedProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Product");
        product.setPrice(10.0);
        product.setStock(100);

        product = productRepository.save(product);

        Mockito.doNothing().when(productService).deleteProduct(product.getId());

        mockMvc.perform(delete("/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    @Test
    public void testDeleteProductNotFound() throws Exception {
        Long productId = 999L;

        Mockito.doThrow(new NoSuchElementException("Product not found")).when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error deleting product: Product not found"));
    }
}
