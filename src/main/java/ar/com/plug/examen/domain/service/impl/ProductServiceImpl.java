package ar.com.plug.examen.domain.service.impl;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.repository.ProductRepository;
import ar.com.plug.examen.domain.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        logger.debug("Creating product with details: {}", product);
        try {
            Product savedProduct = productRepository.save(product);
            logger.debug("Product created successfully: {}", savedProduct);
            return savedProduct;
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        logger.debug("Retrieving all products");
        try {
            List<Product> products = productRepository.findAll();
            logger.debug("Retrieved {} products", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        logger.debug("Retrieving product by id: {}", id);
        try {
            Optional<Product> product = productRepository.findById(id);
            product.ifPresent(p -> logger.debug("Product retrieved successfully: {}", p));
            return product;
        } catch (Exception e) {
            logger.error("Error retrieving product by id: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        logger.debug("Updating product with id: {}", id);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            Product updatedProduct = productRepository.save(product);
            logger.debug("Product updated successfully: {}", updatedProduct);
            return updatedProduct;
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteProduct(Long id) {
        logger.debug("Deleting product with id: {}", id);
        try {
            productRepository.deleteById(id);
            logger.debug("Product deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            throw e;
        }
    }
}
