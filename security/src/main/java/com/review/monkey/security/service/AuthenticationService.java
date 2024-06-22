package com.review.monkey.security.service;

import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    boolean authentication (AuthenticationRequest request);
}
