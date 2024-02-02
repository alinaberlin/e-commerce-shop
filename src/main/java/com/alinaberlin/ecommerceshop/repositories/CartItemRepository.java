package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.CartItem;
import com.alinaberlin.ecommerceshop.models.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
