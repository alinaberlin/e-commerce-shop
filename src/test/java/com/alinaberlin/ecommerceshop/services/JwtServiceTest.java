package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;

class JwtServiceTest {

    @Test
    void shouldExtractUserNameSuccessful() {
        JwtService jwtService = new JwtService(Base64.getEncoder().encodeToString("12736273627hah12736273627hah12736273627hah".getBytes()));
        UserDetails userDetails = new User("Alina", "alina@gmail.com", "134", Role.USER);
        String result = jwtService.generateToken(userDetails);
        String userName = jwtService.extractUserName(result);
        Assertions.assertEquals("alina@gmail.com", userName);
    }

    @Test
    void shouldGenerateTokenSuccessful() {
        JwtService jwtService = new JwtService(Base64.getEncoder().encodeToString("12736273627hah12736273627hah12736273627hah".getBytes()));
        UserDetails userDetails = new User("Alina", "alina@gmail.com", "134", Role.USER);
        String result = jwtService.generateToken(userDetails);
        Assertions.assertNotNull(result);

    }

    @Test
    void tokenShouldBeValid() {
        JwtService jwtService = new JwtService(Base64.getEncoder().encodeToString("12736273627hah12736273627hah12736273627hah".getBytes()));
        UserDetails userDetails = new User("Alina", "alina@gmail.com", "134", Role.USER);
        String result = jwtService.generateToken(userDetails);
        Assertions.assertTrue(jwtService.isTokenValid(result));
    }

    @Test
    void tokenShouldNotBeValid() {
        JwtService jwtService = new JwtService(Base64.getEncoder().encodeToString("12736273627hah12736273627hah12736273627hah".getBytes()));
        UserDetails userDetails = new User("Alina", "alina@gmail.com", "134", Role.USER);
        String result = jwtService.generateToken(userDetails);
        JwtService jwtService2 = new JwtService(Base64.getEncoder().encodeToString("12736273127hah12736273627hah12736273627hah".getBytes()));
        Assertions.assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> jwtService2.isTokenValid(result));
    }
}
