package com.alinaberlin.ecommerceshop.controllers;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderControllerIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static String loginBody = "{\"email\":\"alina@gmail.com\", \"password\":\"12345\" }";
    private User user;
    private Product product;

    private ObjectMapper mapper = new ObjectMapper();

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
        user = userRepository.save(new User("Alina", "alina@gmail.com", passwordEncoder.encode("12345"), Role.USER));
        product = productRepository.save(new Product("Lipstick", "Dior Nude 02", 2, 55.34));
    }

    @Transactional
    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateCart() throws JsonProcessingException {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CART, user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(8080)
                .and().headers(headers)
                .and().body(mapper.writeValueAsString(order))
                .when().post("/orders")
                .then().assertThat().statusCode(201);
    }

    @Test
    void shouldCreateAOrder() throws JsonProcessingException {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order = orderRepository.save(order);
        order.getProducts().add(product);
        orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        UpdateOrderStatus updateOrderStatus = new UpdateOrderStatus(OrderStatus.CREATED);
        given().port(8080)
                .and()
                .headers(headers)
                .and()
                .body(mapper.writeValueAsString(updateOrderStatus))
                .when().patch("/orders/" + order.getId())
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void shouldDeleteOrder() throws JsonProcessingException {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CREATED, user);
        order = orderRepository.save(order);
        order.getProducts().add(product);
        orderRepository.save(order);
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
    void shouldAddProduct() throws JsonProcessingException {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        headers.put("Authorization", "Bearer " + token);
        AddProduct addProduct = new AddProduct(order.getId(), product.getId());
        given().port(8080)
                .and()
                .headers(headers).and().body(mapper.writeValueAsString(addProduct))
                .when().post("/orders/products/add")
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    void shouldGiveAnOrderByIt() {
        String token = getToken();
        Order order = new Order(new Date(), OrderStatus.CART, user);
        order = orderRepository.save(order);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        headers.put("Authorization", "Bearer " + token);
        given().port(8080)
                .and()
                .headers(headers)
                .when().get("/orders/" + order.getId())
                .then()
                .assertThat()
                .statusCode(200);

    }

}
