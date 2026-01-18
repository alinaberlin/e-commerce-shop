package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.CartEmptyException;
import com.alinaberlin.ecommerceshop.exceptions.ForbiddenException;
import com.alinaberlin.ecommerceshop.exceptions.InsuficientStockException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidStateException;
import com.alinaberlin.ecommerceshop.models.entities.Cart;
import com.alinaberlin.ecommerceshop.models.entities.Order;
import com.alinaberlin.ecommerceshop.models.entities.OrderHistory;
import com.alinaberlin.ecommerceshop.models.entities.OrderItem;
import com.alinaberlin.ecommerceshop.models.entities.OrderItemId;
import com.alinaberlin.ecommerceshop.models.entities.OrderStatus;
import com.alinaberlin.ecommerceshop.models.entities.Product;
import com.alinaberlin.ecommerceshop.repositories.OrderItemRepository;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    public static final String NOT_FOUND_MS = "Order not found";
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final CartService cartService;

    private final OrderItemRepository orderItemRepository;
    private final OrderHistoryService orderHistoryService;


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, OrderItemRepository orderItemRepository, OrderHistoryService orderHistoryService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.orderHistoryService = orderHistoryService;
    }

    @Transactional
    public Order createOrder(Long userId) {
        Cart cart = cartService.findByUserId(userId);
        BigDecimal total = cart.getItems()
                .stream()
                .map(item -> item.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.valueOf(0.0), BigDecimal::add);
        Order order = orderRepository.save(new Order(new Date(), OrderStatus.CREATED, cart.getUser(), total));
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }
        Set<OrderItem> items = cart.getItems()
                .stream()
                .map(item -> new OrderItem(new OrderItemId(item.getId().getProductId(), order.getId()),
                        item.getQuantity(), item.getProduct())).collect(Collectors.toSet());
        orderItemRepository.saveAll(items);
        orderHistoryService.save(new OrderHistory(order, order.getOrderStatus(), new Date()));
        cartService.clearCart(cart.getId());
        order.setItems(items);
        return order;
    }

    public Order changeStatus(OrderStatus status, Long id) {
        Order order = null;
        switch (status) {
            case CANCELED -> order = cancelOrder(id);
            case DISPATCHED -> order = changeStatusToDispatched(id);
            case IN_DELIVERY -> order = changeStatusToINDelivery(id);
            case DELIVERED -> order = changeStatusToDelivery(id);

            default -> throw new InvalidStateException("Unexpected value: " + status);
        }
        orderHistoryService.save(new OrderHistory(order, order.getOrderStatus(), new Date()));
        return order;
    }

    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.getItems().forEach(item -> {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + 1);
                productRepository.save(product);
            });
            order.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            return order;
        }
        throw new InvalidStateException("Unexpected value: ");
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

    public List<OrderHistory> getHistory(Long id) {
        return orderHistoryService.findHistoryByOrderId(id);
    }
}
