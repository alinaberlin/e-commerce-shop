package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Cart;
import com.alinaberlin.ecommerceshop.models.Order;
import com.alinaberlin.ecommerceshop.models.OrderStatus;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.repositories.CartItemRepository;
import com.alinaberlin.ecommerceshop.repositories.CartRepository;
import com.alinaberlin.ecommerceshop.repositories.OrderHistoryRepository;
import com.alinaberlin.ecommerceshop.repositories.OrderItemRepository;
import com.alinaberlin.ecommerceshop.repositories.OrderRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import com.alinaberlin.ecommerceshop.repositories.RefreshTokenRepository;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.CartService;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Testcontainers
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {
    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer()
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @LocalServerPort
    private Integer port;
    private static String loginBody = "{\"email\":\"alina@gmail.com\", \"password\":\"12345\" }";
    private User user;
    private Product product;

    private String getToken() {
        Response response = given().port(port).and().header("Content-type", "application/json")
                .and().body(loginBody)
                .when().post("/api/v1/auth/signin")
                .then()
                .extract().response();
        return response.jsonPath().getString("token");
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = userRepository.save(new User("Alina", "alina@gmail.com", passwordEncoder.encode("12345"), Role.USER));
        product = productRepository.save(new Product("Lipstick", "Dior Nude 02", 2, BigDecimal.valueOf(55.34)));
    }

    @Transactional
    @AfterEach
    public void tearDown() {
        refreshTokenRepository.deleteAll();
        orderHistoryRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderSuccessful() {
        cartService.createCart(new Cart(user));
        cartService.addItem(user.getId(), product.getId(), 2);
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        Response response = given().port(port)
                .and()
                .headers(headers)
                .when().post("/orders")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .response();
        Assertions.assertEquals(BigDecimal.valueOf(55.34).multiply(BigDecimal.valueOf(2)),
                BigDecimal.valueOf(response.jsonPath().getDouble("total")));
    }

    @Test
    void shouldReturnCardEmptyErrorMessage() {
        cartService.createCart(new Cart(user));
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        Response response = given().port(port)
                .and()
                .headers(headers)
                .when().post("/orders")
                .then()
                .assertThat()
                .statusCode(417)
                .extract()
                .response();
        Assertions.assertEquals("Cart is empty", response.jsonPath().getString("message"));
    }

    @Test
    void shouldDeleteOrder() {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and()
                .headers(headers)
                .when().delete("/orders/" + order.getId())
                .then()
                .assertThat()
                .statusCode(204);

    }

    @Test
    void shouldGiveAnOrderByIt() {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and()
                .headers(headers)
                .when().get("/orders/" + order.getId())
                .then()
                .assertThat()
                .statusCode(200);

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
