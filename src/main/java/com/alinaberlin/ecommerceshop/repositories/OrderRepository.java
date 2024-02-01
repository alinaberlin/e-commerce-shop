package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUserId(Long id);
    List<Order> findOrderByOrderStatusAndUserId(OrderStatus orderStatus, Long id);
}
