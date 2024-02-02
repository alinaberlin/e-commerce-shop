package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderRepository> {
}
