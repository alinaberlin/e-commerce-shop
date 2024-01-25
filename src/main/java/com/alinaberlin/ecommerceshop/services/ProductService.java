package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findByName(String name) {

        return productRepository.findProductByName(name).orElse(null);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new InvalidIdException("Product not found"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product createOrUpdate(Product product) {
        if (product.getId() != null) {
            findById(product.getId());
        }
        return productRepository.save(product);
    }

}

