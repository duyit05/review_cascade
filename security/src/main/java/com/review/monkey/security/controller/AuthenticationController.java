package com.review.monkey.security.controller;

import com.nimbusds.jose.JOSEException;
import com.review.monkey.security.error.ApiError;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.request.IntrospectRequest;
import com.review.monkey.security.request.LogoutRequest;
import com.review.monkey.security.request.RefreshTokenRequest;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.response.IntrospectResponse;
import com.review.monkey.security.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RequestMapping("/auth")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws JOSEException {
        AuthenticationResponse result = authenticationService.authentication(request);
//        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//        String displayKey = Encoders.BASE64.encode(secretKey.getEncoded());
//        System.out.println(displayKey);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .message("LOGOUT SUCCESSFULLY")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
       return ApiResponse.<AuthenticationResponse>builder()
               .result(authenticationService.refreshToken(request))
               .build();
    }
}
