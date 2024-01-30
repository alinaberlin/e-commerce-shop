package com.alinaberlin.ecommerceshop.controllers;


import com.alinaberlin.ecommerceshop.exceptions.InvalidStateException;
import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderStatus;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.AddProduct;
import com.alinaberlin.ecommerceshop.payloads.UpdateOrderStatus;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.OrderService;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private OrderRepository orderRepository;
    private OrderController orderController;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Principal principal;
    @Mock
    private Product product;

    @BeforeEach
    public void setUp() {
        orderController = new OrderController(new OrderService(orderRepository, productRepository), userRepository);
    }

    @Test
    void shouldGetAllOrders() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order.setId(12L);
        Order order2 = new Order(new Date(), OrderStatus.CART, user);
        order2.setId(13L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.findUserByEmail(principal.getName())).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findOrderByUserId(user.getId())).thenReturn(List.of(order, order2));
        ResponseEntity<List<Order>> result = orderController.getAllOrders(principal);
        Assertions.assertEquals(List.of(order, order2), result.getBody());
    }

    @Test
    void shouldGetOrderById() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order.setId(1L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity<Order> result = orderController.getOrderById(principal, order.getId());
        Assertions.assertEquals(order, result.getBody());
    }

    @Test
    void shouldCreateACart() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(1L);
        Order order2 = new Order(new Date(), OrderStatus.CART, user);
        order2.setId(1L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.findUserByEmail(principal.getName())).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.save(order2)).thenReturn(order2);
        ResponseEntity<Order> result = orderController.createCart(principal, order);
        Assertions.assertEquals(order2.getOrderStatus(), result.getBody().getOrderStatus());
    }

    @Test
    void shouldCancelOrder() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(1L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.of(order));
        Order order2 = new Order(new Date(), OrderStatus.CANCELED, user);
        order2.setId(1L);
        Mockito.when(orderRepository.save(order2)).thenReturn(order2);
        ResponseEntity<Order> result = orderController.cancelOrder(principal, 1L);
        Assertions.assertEquals(order2.getOrderStatus(), Objects.requireNonNull(result.getBody()).getOrderStatus());
    }
    @Test
    void shouldNotCancelOrder() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.DISPATCHED, user);
        order.setId(1L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.of(order));
        Assertions.assertThrows(InvalidStateException.class, () -> orderController.cancelOrder(principal, 1L));
    }


    @Test
    void shouldAddProduct() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order.setId(1L);
        Product product = new Product("Lipgloss", "DevotionNo Transfer Matte Liquid Lip GRATITUDINE 200", 2, 34.95);
        product.setId(4L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(4L)).thenReturn(Optional.of(product));
        ResponseEntity<Order> result = orderController.addProduct(principal, new AddProduct(1L, 4L));
        Assertions.assertEquals(1, Objects.requireNonNull(result.getBody()).getProducts().size());
    }
    @Test
    void shouldChangeStatusToDispatch(){
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(1L);
        Product product = new Product("Lipgloss", "DevotionNo Transfer Matte Liquid Lip GRATITUDINE 200", 2, 34.95);
        product.setId(4L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order order2 = new Order(new Date(), OrderStatus.DISPATCHED, user);
        order2.setId(1L);
        Mockito.when(orderRepository.save(order2)).thenReturn(order2);
        ResponseEntity<Order> result = orderController.changeStatus(principal, new UpdateOrderStatus(order2.getOrderStatus()), 1L);
        Assertions.assertEquals(OrderStatus.DISPATCHED, Objects.requireNonNull(result.getBody()).getOrderStatus());


    }
}

