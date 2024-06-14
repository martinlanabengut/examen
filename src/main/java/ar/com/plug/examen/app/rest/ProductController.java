package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Received request to create product: {}", product);
        try {
            Product createdProduct = productService.createProduct(product);
            logger.info("Product created successfully: {}", createdProduct);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to get all products");
        try {
            List<Product> products = productService.getAllProducts();
            logger.info("Retrieved {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Received request to get product by id: {}", id);
        try {
            return productService.getProductById(id)
                    .map(product -> {
                        logger.info("Product retrieved successfully: {}", product);
                        return ResponseEntity.ok(product);
                    })
                    .orElseGet(() -> {
                        logger.warn("Product with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error retrieving product by id: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        logger.info("Received request to update product with id: {}", id);
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            logger.info("Product updated successfully: {}", updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with id: {}", id);
        try {
            productService.deleteProduct(id);
            logger.info("Product deleted successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
