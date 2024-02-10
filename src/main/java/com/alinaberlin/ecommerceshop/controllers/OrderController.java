package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderHistory;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.UpdateOrderStatus;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        Order order = orderService.createOrder(user.getId());
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        List<Order> orders = orderService.findOrderByUserId(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(Principal principal, @PathVariable Long id) {
        Order order = orderService.checksRightsOnOrder(id, principal.getName());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> cancelOrder(Principal principal, @PathVariable Long id) {
        Order order = orderService.checksRightsOnOrder(id, principal.getName());
        Order canceledOrder = orderService.cancelOrder(order.getId());
        return new ResponseEntity<>(canceledOrder, HttpStatus.NO_CONTENT);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> changeStatus(Principal principal, @RequestBody UpdateOrderStatus updateOrderStatus, @PathVariable Long id) {
        Order order = orderService.checksRightsOnOrder(id, principal.getName());
        Order orderStatus = orderService.changeStatus(updateOrderStatus.status(), order.getId());
        return new ResponseEntity<>(orderStatus, HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderHistory>> getHistory(Principal principal, @PathVariable Long id) {
        orderService.checksRightsOnOrder(id, principal.getName());
        List<OrderHistory> histories = orderService.getHistory(id);
        return new ResponseEntity<>(histories, HttpStatus.OK);
    }
}
