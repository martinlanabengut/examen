package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import java.util.NoSuchElementException;
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

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    @ApiOperation(value = "Create a new product", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created product"),
            @ApiResponse(code = 400, message = "Invalid product data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Received request to create product: {}", product);
        try {
            Product createdProduct = productService.createProduct(product);
            logger.info("Product created successfully: {}", createdProduct);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get all products.
     *
     * @return the list of all products
     */
    @ApiOperation(value = "View a list of available products", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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

    /**
     * Get product by ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the specified ID
     */
    @ApiOperation(value = "Get a product by Id", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved product"),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
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

    /**
     * Update an existing product.
     *
     * @param id the ID of the product to update
     * @param productDetails the new details of the product
     * @return the updated product
     */
    @ApiOperation(value = "Update an existing product", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated product"),
            @ApiResponse(code = 400, message = "Invalid product data"),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        logger.info("Received request to update product with id: {}", id);
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            logger.info("Product updated successfully: {}", updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            logger.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete a product.
     *
     * @param id the ID of the product to delete
     * @return a response entity with a success message
     */
    @ApiOperation(value = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted product"),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with id: {}", id);
        try {
            productService.deleteProduct(id);
            logger.info("Product deleted successfully");
            return ResponseEntity.ok("Product deleted successfully");
        } catch (NoSuchElementException e) {
            logger.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(404).body("Error deleting product: Product not found");
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error deleting product: " + e.getMessage());
        }
    }
}
