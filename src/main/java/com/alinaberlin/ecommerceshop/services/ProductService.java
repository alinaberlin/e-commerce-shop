package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.models.ProductMapper;
import com.alinaberlin.ecommerceshop.models.dtos.ProductDTO;
import com.alinaberlin.ecommerceshop.models.entities.Product;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Product findByName(String name) {

        return productRepository.findProductByName(name).orElse(null);
    }
    public Page<Product> findAll(int pageNum, int size){
        PageRequest request = PageRequest.of(pageNum, size);
        return productRepository.findAll(request);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new InvalidIdException("Product not found"));
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(productMapper::fromEntity).toList();
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

