package com.review.monkey.security.service;

import com.nimbusds.jose.JOSEException;

import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponse authentication (AuthenticationRequest request) throws JOSEException;


}
