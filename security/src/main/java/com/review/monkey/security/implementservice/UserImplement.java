package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.UserMapper;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserImplement implements UserService {
    UserRespository userRespository;
    UserMapper userMapper;


    @Override
    public List<UserResponse> getAllUser() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> userList = userRespository.findAll();
        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getUserId());
            userResponse.setUsername(user.getUsername());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRespository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       user.setPassword(passwordEncoder.encode(request.getPassword()));

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
    public UserResponse getUserById(int userId) {
        return userMapper.toUserResponse(userRespository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID : " + userId)));
    }
}
