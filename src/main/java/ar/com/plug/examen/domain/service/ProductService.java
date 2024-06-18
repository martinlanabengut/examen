package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
}

