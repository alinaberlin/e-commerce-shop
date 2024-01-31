package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.SignUpRequest;
import com.alinaberlin.ecommerceshop.payloads.SigninRequest;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationControlerTest {
    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSignupSuccessfull() throws JsonProcessingException {
        SignUpRequest signUpRequest = new SignUpRequest("alina@gmail.com", "1234567", "alina");
        given().port(8080).and()
                .header("Content-type", "application/json")
                .and().body(mapper.writeValueAsString(signUpRequest))
                .when()
                .post("/api/v1/auth/signup").then().assertThat().statusCode(200);
    }

    @Test
    void shouldSigninSuccessfull() throws JsonProcessingException {
       User user = userRepository.save(new User("Alina", "alina@gmail.com", passwordEncoder.encode("12345"), Role.USER));
        SigninRequest signinRequest = new SigninRequest("alina@gmail.com", "12345");
        given().port(8080).and()
                .header("Content-type", "application/json")
                .and().body(mapper.writeValueAsString(signinRequest))
                .when()
                .post("/api/v1/auth/signin").then().assertThat().statusCode(200);

    }
}