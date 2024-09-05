package com.review.monkey.security.repository;


import com.review.monkey.security.model.User;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , String> , JpaSpecificationExecutor<User> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername (String username);
}
