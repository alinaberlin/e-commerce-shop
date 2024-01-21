package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Order order){
        return orderRepository.save(order);
    }
    public Order addProduct(Order order,Long id){
      Product product =  productRepository.findById(id).orElseThrow();
      if(product.getQuantity()>1){
          order.getProducts().add(product);
          orderRepository.save(order);
          product.setQuantity(product.getQuantity()-1);
          productRepository.save(product);
          return order;
      }
      throw new RuntimeException("Product doesn't has enought quantity");
    }

}
