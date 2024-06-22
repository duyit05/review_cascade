package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationImplement implements AuthenticationService {

    UserRespository userRespository;

    @Override
    public boolean authentication(AuthenticationRequest request) {
        User checkUserExisted = userRespository.findByUsername(request.getUsername())
                                .orElseThrow(()-> new  AppException(ErrorCode.USER_NOT_EXIST));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword() , checkUserExisted.getPassword());

    }
}
