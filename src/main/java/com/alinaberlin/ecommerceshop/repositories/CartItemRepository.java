package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.entities.CartItem;
import com.alinaberlin.ecommerceshop.models.entities.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
