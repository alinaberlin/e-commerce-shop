package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.JwtAuthenticationResponse;
import com.alinaberlin.ecommerceshop.payloads.SignUpRequest;
import com.alinaberlin.ecommerceshop.payloads.SigninRequest;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = new User(request.getName(), request.getUsername(), passwordEncoder.encode(request.getPassword()), Role.USER);;
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

}
