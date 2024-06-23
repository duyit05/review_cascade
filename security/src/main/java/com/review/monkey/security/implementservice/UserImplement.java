package com.review.monkey.security.implementservice;

import com.review.monkey.security.enums.Roles;
import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.UserMapper;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.request.update.UserUpdateRequest;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserImplement implements UserService {


    UserRespository userRespository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    @Override
    // CHECK CONDITION FIRST IF CONDITION TRUE THEN ACCESS THIS FUNCTION
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser() {
        log.info("THIS IS FUNCTION GET ALL USER");
        return userRespository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    @Override
    // ENABLE ACCESS THIS FUNCTION AFTER CHECK CONDITION
    //@PostAuthorize("hasRole('ADMIN')")

    // CHECK CONDITION USERNAME LOGGING  RIGHT WITH ID HAVE USERNAME OR NOT
    // SUCH AS LOGGING WITH USERNAME ARE duy10 THEN GET BY ID OF THIS USERNAME , ANOTHER USERNAME THEN FORBIDENCE
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(int userId) {
        log.info("THIS IS FUNCTION GET USER BY ID");
        return userMapper.toUserResponse(userRespository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID : " + userId)));
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRespository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Roles.USER.name());

        user.setRoles(roles);

        return userMapper.toUserResponse(userRespository.save(user));
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRespository.findById(userId).orElseThrow(() -> new RuntimeException("CAN'T DELETE USER WITH ID : " + userId));
        userRespository.delete(user);
    }

    @Override
    public UserResponse updateUser(int userId, UserRequest request) {
        User user = userRespository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID : " + userId));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRespository.save(user));
    }

    @Override
    public UserResponse getMyInfo(UserUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRespository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USERNAME_INVALID));
        userMapper.updateUserInfo(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRespository.save(user));
    }

}
