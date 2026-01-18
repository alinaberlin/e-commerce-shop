package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.entities.OrderHistory;
import com.alinaberlin.ecommerceshop.repositories.OrderHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public List<OrderHistory> findHistoryByOrderId(Long id){
        return orderHistoryRepository.findOrderHistoriesByOrder_Id(id);
    }
    public OrderHistory save(OrderHistory orderHistory){
        return orderHistoryRepository.save(orderHistory);
    }

}
