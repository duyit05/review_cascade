package com.review.monkey.security.controller;

import com.nimbusds.jose.JOSEException;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class AuthenticaionController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> login (@RequestBody AuthenticationRequest request) throws JOSEException {
        AuthenticationResponse result = authenticationService.authentication(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
