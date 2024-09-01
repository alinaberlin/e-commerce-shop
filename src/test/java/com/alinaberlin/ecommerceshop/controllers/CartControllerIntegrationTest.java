package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Cart;
import com.alinaberlin.ecommerceshop.models.CartItem;
import com.alinaberlin.ecommerceshop.models.CartItemId;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.Item;
import com.alinaberlin.ecommerceshop.repositories.CartItemRepository;
import com.alinaberlin.ecommerceshop.repositories.CartRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import com.alinaberlin.ecommerceshop.repositories.RefreshTokenRepository;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
@Testcontainers
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerIntegrationTest {
    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer()
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private static String loginBody = "{\"email\":\"alina@gmail.com\", \"password\":\"12345\" }";
    private User user;
    private Product product;
    @LocalServerPort
    private Integer port;

    private ObjectMapper mapper = new ObjectMapper();

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
        user = userRepository.save(new User("Alina", "alina@gmail.com", passwordEncoder.encode("12345"), Role.USER));
        product = productRepository.save(new Product("Lipstick", "Dior Nude 02", 2, BigDecimal.valueOf(55.34)));
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateCartSuccessful() {
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and().headers(headers)
                .when().post("/cart")
                .then().assertThat().statusCode(201);
    }

    @Test
    void shouldAddItemSuccessful() throws JsonProcessingException {
        cartService.createCart(new Cart(user));
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and().headers(headers)
                .and().body(mapper.writeValueAsString(new Item(product.getId(), 2)))
                .when().post("/cart/item/add")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("product.name", equalTo(product.getName()));
    }

    @Test
    void shouldUpdateItemSuccessful() throws JsonProcessingException {
        cartService.createCart(new Cart(user));
        cartService.addItem(user.getId(), product.getId(), 2);
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and().headers(headers)
                .and().body(mapper.writeValueAsString(new Item(product.getId(), 1)))
                .when().post("/cart/item/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("product.quantity", equalTo(1));
    }

    @Test
    void shouldDeleteItemSuccessful() throws JsonProcessingException {
        cartService.createCart(new Cart(user));
        cartService.addItem(user.getId(), product.getId(), 2);
        String token = getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        given().port(port)
                .and().headers(headers)
                .when().delete("/cart/item/" + product.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
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
