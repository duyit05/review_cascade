package com.review.monkey.security.implementservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;

import com.nimbusds.jwt.SignedJWT;
import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.request.IntrospectRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.response.IntrospectResponse;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.AuthenticationService;
import com.review.monkey.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationImplement implements AuthenticationService {

    UserRespository userRespository;
    JwtService jwtService;



    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        User checkUserExisted = userRespository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), checkUserExisted.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = jwtService.generateToken(checkUserExisted);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // GET TOKEN FROM REQUEST
        String token = request.getToken();

        // CHECK KEY TRUE OR FALSE
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // PARSE TOKEN FROM REQUEST
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.HEADER_INVALID);
        }
        // CHECK TOKEN HAS BEEN TIME OR NOT
        Date expritime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // VERIFIER
        boolean verified = signedJWT.verify(verifier);

        if (!expritime.after(new Date()) && verified)
            throw new AppException(ErrorCode.HEADER_INVALID);

        return IntrospectResponse.builder().valid(verified).build();
    }
}
