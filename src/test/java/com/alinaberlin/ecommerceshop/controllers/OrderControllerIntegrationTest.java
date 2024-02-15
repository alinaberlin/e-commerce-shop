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
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderControllerIntegrationTest {
    @ClassRule
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
    private static String loginBody = "{\"email\":\"alina@gmail.com\", \"password\":\"12345\" }";
    private User user;
    private Product product;

    private String getToken() {
        Response response = given().port(8080).and().header("Content-type", "application/json")
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
        orderHistoryRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderSuccessful() throws JsonProcessingException {
        cartService.createCart(new Cart(user));
        cartService.addItem(user.getId(), product.getId(), 2);
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        Response response = given().port(8080)
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
    void shouldDeleteOrder() {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(8080)
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
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(8080)
                .and()
                .headers(headers)
                .when().get("/orders/" + order.getId())
                .then()
                .assertThat()
                .statusCode(200);

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

}
