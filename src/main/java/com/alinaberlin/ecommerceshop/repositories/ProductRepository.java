package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByName(String name);

    @Override
    Page<Product> findAll(Pageable pageable);
}
