package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.ForbiddenException;
import com.alinaberlin.ecommerceshop.exceptions.InsuficientStockException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidStateException;
import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderStatus;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    public static final String NOT_FOUND_MS = "Order not found";
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createCard(Order card ) {
        List<Order> current = orderRepository.findOrderByOrderStatusAndUserId(OrderStatus.CART, card.getUser().getId());
        if (current.isEmpty()) {
            card.setOrderStatus(OrderStatus.CART);
            return orderRepository.save(card);
        }
        return current.get(0);
    }

    public Order createOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.CART) {
            order.setOrderStatus(OrderStatus.CREATED);
            return orderRepository.save(order);
        }
        throw new InvalidStateException("Order cannot create order because current status is " + order.getOrderStatus());
    }

    public Order addProduct(Order order, Long id) {
        if (order.getOrderStatus() != OrderStatus.CART) {
            throw new InvalidStateException("This is not a cart");
        }
        Product product = productRepository.findById(id).orElseThrow();
        if (product.getQuantity() > 1 && order.getProducts().add(product)) {
            orderRepository.save(order);
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
            return order;
        }
        throw new InsuficientStockException("Product doesn't has enough quantity");
    }

    public Order changeStatus(OrderStatus status, Long id) {
        Order order = null;
        switch (status) {
            case CREATED -> order = createOrder(id);
            case CANCELED -> order = cancelOrder(id);
            case DISPATCHED -> order = changeStatusToDispatched(id);
            case IN_DELIVERY -> order = changeStatusToINDelivery(id);
            case DELIVERED -> order = changeStatusToDelivery(id);

            default -> throw new InvalidStateException("Unexpected value: " + status);
        }
        return order;
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
        Order order = orderRepository.findById(id).orElseThrow(() -> new InvalidIdException(NOT_FOUND_MS));
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.setOrderStatus(OrderStatus.DISPATCHED);
            return orderRepository.save(order);
        }
        throw new InvalidStateException("Order cannot move to dispatched because current status is " + order.getOrderStatus());
    }

    public Order changeStatusToINDelivery(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new InvalidIdException(NOT_FOUND_MS));
        if (order.getOrderStatus() == OrderStatus.DISPATCHED) {
            order.setOrderStatus(OrderStatus.IN_DELIVERY);
            return orderRepository.save(order);
        }
        throw new InsuficientStockException("Order cannot move to in-delivery because current status is " + order.getOrderStatus());
    }

    public Order changeStatusToDelivery(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new InvalidIdException(NOT_FOUND_MS));
        if (order.getOrderStatus() == OrderStatus.IN_DELIVERY) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            return orderRepository.save(order);
        }
        throw new InvalidStateException("Order cannot move to delivery because the current status is " + order.getOrderStatus());
    }

    public List<Order> findOrderByUserId(Long id) {
        return orderRepository.findOrderByUserId(id);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new InvalidIdException(NOT_FOUND_MS));
    }

    public Order checksRightsOnOrder(Long id, String userName) {
        Order order = findById(id);
        if (order.getUser().getUsername().equals(userName)) {
            return order;
        }
        throw new ForbiddenException("Current user doesn't has right");
    }
}
