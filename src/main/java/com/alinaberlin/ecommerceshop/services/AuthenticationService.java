package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.models.RefreshToken;
import com.alinaberlin.ecommerceshop.models.Role;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.JwtAuthenticationResponse;
import com.alinaberlin.ecommerceshop.payloads.RefreshTokenRequest;
import com.alinaberlin.ecommerceshop.payloads.SignUpRequest;
import com.alinaberlin.ecommerceshop.payloads.SigninRequest;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
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

    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = new User(request.getName(), request.getUsername(), passwordEncoder.encode(request.getPassword()), Role.USER);
        userRepository.save(user);
        return buildAuthenticationResponse(user);
    }

    @Transactional
    public JwtAuthenticationResponse signIn(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        return buildAuthenticationResponse(user);
    }

    @Transactional
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.verifyExpiration(request.getRefreshToken());
        User user = token.getUser();
        return buildAuthenticationResponse(user);
    }

    @NotNull
    private JwtAuthenticationResponse buildAuthenticationResponse(User user) {
        String jwt = jwtService.generateToken(user);
        RefreshToken refreshToken= refreshTokenService.createRefreshToken(user.getId());
        return new JwtAuthenticationResponse(jwt, refreshToken.getToken());
    }

}
