package com.review.monkey.security.service;

import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.request.update.UserUpdateRequest;
import com.review.monkey.security.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserResponse> getAllUser ();
    UserResponse createUser (UserRequest request);
    void deleteUser (int userId);

    UserResponse updateUser(int userId , UserRequest request);

    UserResponse getUserById (int userId);
    UserResponse getMyInfo (UserUpdateRequest request);

}
