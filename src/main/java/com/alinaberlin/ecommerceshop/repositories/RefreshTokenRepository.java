package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.entities.RefreshToken;
import com.alinaberlin.ecommerceshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);
    @Modifying
    int deleteByUser(User user);
}
