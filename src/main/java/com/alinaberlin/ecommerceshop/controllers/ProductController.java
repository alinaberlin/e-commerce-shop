package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.services.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.introspector.Property;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    public ProductService productService;

    //GET/ to list all properties/to list a product
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //post/product to create a new product
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct = productService.createOrUpdate(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Long id, @RequestBody Product product) {
        if (!Objects.equals(product.getId(), id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product createdProduct = productService.createOrUpdate(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }
    //Delete/product/{id} to delete a property listing
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        Product product = productService.findById(id);
        if(product ==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
