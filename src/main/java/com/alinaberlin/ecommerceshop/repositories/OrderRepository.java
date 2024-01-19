package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUserId(Long id);
    List<Order> findOrderByOrderTypeAndUserId(OrderType type, Long id);
}
