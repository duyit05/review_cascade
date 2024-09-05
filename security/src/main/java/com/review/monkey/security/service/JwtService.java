package com.review.monkey.security.service;


import com.review.monkey.security.model.User;
import com.review.monkey.security.request.LogoutRequest;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken (User user);

    String buildScope(User user);


}
