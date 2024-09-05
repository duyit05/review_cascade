package com.review.monkey.security.repository;

import com.review.monkey.security.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
