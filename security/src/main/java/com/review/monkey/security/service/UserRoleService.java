package com.review.monkey.security.service;

import com.review.monkey.security.request.UserRoleRequest;
import com.review.monkey.security.response.UserRoleResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {
    UserRoleResponse createUserRole (int userId , int roleId);
    UserRoleResponse updateUserRole (int userRoleId , int roleId);

    void deleteUserRoleById (int userRoleId);

}
