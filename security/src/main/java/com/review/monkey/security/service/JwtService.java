package com.review.monkey.security.service;


import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken (String username);
}
