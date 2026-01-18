package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.entities.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findOrderHistoriesByOrder_Id(Long id);
}
