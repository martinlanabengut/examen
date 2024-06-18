package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.repository.ProductRepository;
import ar.com.plug.examen.domain.service.ProductService;
import ar.com.plug.examen.domain.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl; // Implementation of the service

    private ProductService productService; // Interface of the service

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = productServiceImpl; // Assign the implementation to the interface reference
    }

    @Test
    @DisplayName("Should create a new product successfully")
    void testCreateProduct() {
        // Given
        Product product = new Product();
        product.setName("Test Product");

        // When
        when(productRepository.save(product)).thenReturn(product);
        Product createdProduct = productService.createProduct(product);

        // Then
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw exception when creating product fails")
    void testCreateProductThrowsException() {
        // Given
        Product product = new Product();
        product.setName("Test Product");

        // When
        when(productRepository.save(product)).thenThrow(new RuntimeException("Error creating product"));
        Exception exception = assertThrows(RuntimeException.class, () -> productService.createProduct(product));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error creating product");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        // Given
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);

        // When
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(2);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when retrieving all products fails")
    void testGetAllProductsThrowsException() {
        // When
        when(productRepository.findAll()).thenThrow(new RuntimeException("Error retrieving products"));
        Exception exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving products");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return product by ID")
    void testGetProductById() {
        // Given
        Product product = new Product();
        product.setId(1L);

        // When
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retrieving product by ID fails")
    void testGetProductByIdThrowsException() {
        // When
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Error retrieving product by id"));
        Exception exception = assertThrows(RuntimeException.class, () -> productService.getProductById(1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving product by id");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Product");
        updatedDetails.setPrice(10.0);
        updatedDetails.setStock(20);

        // When
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        Product updatedProduct = productService.updateProduct(1L, updatedDetails);

        // Then
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw exception when updating product fails")
    void testUpdateProductThrowsException() {
        // Given
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Product");

        // When
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Error updating product"));
        Exception exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, updatedDetails));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error updating product");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct() {
        // Given
        long productId = 1L;

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Should throw exception when deleting product fails")
    void testDeleteProductThrowsException() {
        // Given
        long productId = 1L;

        // When
        doThrow(new RuntimeException("Error deleting product")).when(productRepository).deleteById(productId);
        Exception exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct(productId));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error deleting product");
        verify(productRepository, times(1)).deleteById(productId);
    }
}
