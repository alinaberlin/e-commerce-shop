package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.TokenRefreshException;
import com.alinaberlin.ecommerceshop.models.entities.RefreshToken;
import com.alinaberlin.ecommerceshop.models.entities.User;
import com.alinaberlin.ecommerceshop.repositories.RefreshTokenRepository;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final Long REFRESH_TOKEN_EXP_IN_MS = (long) (7 * 24 * 60 * 60 * 1000);

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).get();
        RefreshToken refreshToken =  refreshTokenRepository.findByUser(user).orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXP_IN_MS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(String token) {
        RefreshToken dbToken = refreshTokenRepository.findByToken(token).get();
        if (dbToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(dbToken);
            throw new TokenRefreshException(dbToken.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return dbToken;
    }

    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Transactional
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }
}
