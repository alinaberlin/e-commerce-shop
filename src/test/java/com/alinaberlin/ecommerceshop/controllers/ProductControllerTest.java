package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import com.alinaberlin.ecommerceshop.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductRepository productRepository;
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        productController = new ProductController(new ProductService(productRepository));
    }

    @Test
    void getAllProducts() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(3L);
        Product product2 = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product2.setId(2L);
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product, product2));
        ResponseEntity<List<Product>> result = productController.getAllProducts();
        Assertions.assertEquals(List.of(product, product2), result.getBody());

    }

    @Test
    void shouldGetProductById() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(3L);
        Mockito.when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        ResponseEntity<Product> result = productController.getProductById(3L);
        Assertions.assertEquals(product, result.getBody());
    }

    @Test
    void getProductByIdShouldThrowException() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidIdException.class, () -> productController.getProductById(2L));
    }

    @Test
    void shouldCreateProductSucessful() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(1L);
        Product product2 = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        Mockito.when(productRepository.save(product2)).thenReturn(product);
        ResponseEntity<Product> result = productController.create(product2);
        Assertions.assertEquals(product, result.getBody());
    }

    @Test
    void shouldUpdateProductSuccessful() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(1L);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        ResponseEntity<Product> result = productController.update(1L, product);
        Assertions.assertEquals(product, result.getBody());
    }

    @Test
    void updateProductWrongPathId() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(1L);
        ResponseEntity<Product> result = productController.update(2L, product);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void
    deleteProduct() {
        Product product = new Product("Cheese", "Very good creamy cheese", 2, BigDecimal.valueOf(20.00));
        product.setId(1L);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ResponseEntity<Product> result = productController.deleteProduct(1L);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}
