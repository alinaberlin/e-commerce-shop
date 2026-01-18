package com.alinaberlin.ecommerceshop.controllers;


import com.alinaberlin.ecommerceshop.exceptions.InvalidStateException;
import com.alinaberlin.ecommerceshop.models.entities.Order;
import com.alinaberlin.ecommerceshop.models.entities.OrderStatus;
import com.alinaberlin.ecommerceshop.models.entities.Product;
import com.alinaberlin.ecommerceshop.models.entities.Role;
import com.alinaberlin.ecommerceshop.models.entities.User;
import com.alinaberlin.ecommerceshop.payloads.UpdateOrderStatus;
import com.alinaberlin.ecommerceshop.repositories.OrderItemRepository;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.CartService;
import com.alinaberlin.ecommerceshop.services.OrderHistoryService;
import com.alinaberlin.ecommerceshop.services.OrderService;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @ClassRule
    public static MySQLContainer mySQLContainer = new MySQLContainer()
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Principal principal;
    @Mock
    private Product product;

    @Mock
    private CartService cartService;
    @Mock
    private OrderItemRepository orderItemRepository;
    private OrderController orderController;
    @Mock
    private OrderHistoryService orderHistoryService;

    @BeforeEach
    public void setUp() {
        orderController = new OrderController(new OrderService(orderRepository, productRepository, cartService, orderItemRepository, orderHistoryService), userRepository);
    }

    @Test
    void shouldGetAllOrders() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(12L);
        Order order2 = new Order(new Date(), OrderStatus.CREATED, user);
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
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(1L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        ResponseEntity<Order> result = orderController.getOrderById(principal, order.getId());
        Assertions.assertEquals(order, result.getBody());
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
    void shouldChangeStatusToDispatch() {
        User user = new User("Trei", "alina@gmail.com", "123456", Role.ADMIN);
        user.setId(1);
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order.setId(1L);
        Product product = new Product("Lipgloss", "DevotionNo Transfer Matte Liquid Lip GRATITUDINE 200", 2, BigDecimal.valueOf(34.95));
        product.setId(4L);
        Mockito.when(principal.getName()).thenReturn(user.getUsername());
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order order2 = new Order(new Date(), OrderStatus.DISPATCHED, user);
        order2.setId(1L);
        Mockito.when(orderRepository.save(order2)).thenReturn(order2);
        ResponseEntity<Order> result = orderController.changeStatus(principal, new UpdateOrderStatus(order2.getOrderStatus()), 1L);
        Assertions.assertEquals(OrderStatus.DISPATCHED, Objects.requireNonNull(result.getBody()).getOrderStatus());


    }
    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
    @DynamicPropertySource
    static void initialize(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add(
                "spring.datasource.password", mySQLContainer::getPassword);
    }
}

