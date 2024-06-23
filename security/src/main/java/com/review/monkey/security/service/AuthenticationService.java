package com.review.monkey.security.service;

import com.nimbusds.jose.JOSEException;

import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.request.IntrospectRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.response.IntrospectResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AuthenticationService {
    AuthenticationResponse authentication (AuthenticationRequest request) throws JOSEException;

    IntrospectResponse introspect (IntrospectRequest request) throws JOSEException, ParseException;
}
