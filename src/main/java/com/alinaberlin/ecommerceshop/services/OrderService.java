package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderStatus;
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

    public Order createOrder(Order order) {
        order.setOrderStatus(OrderStatus.CREATED);
        return orderRepository.save(order);
    }

    public Order addProduct(Order order, Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        if (product.getQuantity() > 1) {
            order.getProducts().add(product);
            orderRepository.save(order);
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
            return order;
        }
        throw new RuntimeException("Product doesn't has enought quantity");
    }

    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.getProducts().forEach(product -> {
                product.setQuantity(product.getQuantity() + 1);
                productRepository.save(product);
            });
            order.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }

        return order;
    }


    public Order changeStatusToDispatched(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.setOrderStatus(OrderStatus.DISPATCHED);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order cannot move to dispatched because current status is " + order.getOrderStatus());
    }

    public Order changeStatusToINDelivery(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.DISPATCHED) {
            order.setOrderStatus(OrderStatus.IN_DELIVERY);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order cannot move to in-delivery because current status is " + order.getOrderStatus());
    }

    public Order changeStatusToDelivery(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.IN_DELIVERY) {
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order cannot move to delivery because the current status is " + order.getOrderStatus());
    }
}