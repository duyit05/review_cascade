package com.review.monkey.security.implementservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.service.AuthenticationService;
import com.review.monkey.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationImplement implements AuthenticationService {

    UserRespository userRespository;
    JwtService jwtService;

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        User checkUserExisted = userRespository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), checkUserExisted.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = jwtService.generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
}
