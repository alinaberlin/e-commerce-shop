package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String username);
}
