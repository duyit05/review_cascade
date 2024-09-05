package com.review.monkey.security.service;

import com.review.monkey.security.request.UserRoleRequest;
import com.review.monkey.security.response.UserRoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRoleService {
    List<UserRoleResponse> getAllUserAndRole ();
    UserRoleResponse createUserRole (String userId , String roleId);
    UserRoleResponse updateUserRole (int userRoleId , String roleId);

    void deleteUserRoleById (int userRoleId);

}
