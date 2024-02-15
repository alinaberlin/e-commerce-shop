package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.SignUpRequest;
import com.alinaberlin.ecommerceshop.payloads.SigninRequest;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
@Testcontainers
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControlerTest {
    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer()
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private Integer port;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void shouldSignupSuccessfull() throws JsonProcessingException {
        SignUpRequest signUpRequest = new SignUpRequest("alina@gmail.com", "1234567", "alina");
        given().port(port).and()
                .header("Content-type", "application/json")
                .and().body(mapper.writeValueAsString(signUpRequest))
                .when()
                .post("/api/v1/auth/signup").then().assertThat().statusCode(200);
    }

    @Test
    void shouldSigninSuccessfull() throws JsonProcessingException {
       User user = userRepository.save(new User("Alina", "alina@gmail.com", passwordEncoder.encode("12345"), Role.USER));
        SigninRequest signinRequest = new SigninRequest("alina@gmail.com", "12345");
        given().port(port).and()
                .header("Content-type", "application/json")
                .and().body(mapper.writeValueAsString(signinRequest))
                .when()
                .post("/api/v1/auth/signin").then().assertThat().statusCode(200);

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