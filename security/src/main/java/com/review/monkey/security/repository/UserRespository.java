package com.review.monkey.security.repository;


import com.review.monkey.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository  extends JpaRepository<User , Integer> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername (String username);
}
