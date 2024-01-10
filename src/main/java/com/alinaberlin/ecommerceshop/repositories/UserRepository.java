package com.alinaberlin.ecommerceshop.repositories;

import com.alinaberlin.ecommerceshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
