package com.review.monkey.security.service;

import com.review.monkey.security.request.RoleRequest;
import com.review.monkey.security.response.RoleResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface RoleService {
    List<RoleResponse> getAllRole();

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRoleById(String roleId, RoleRequest request);

    void deleteRoleById(String roleId);

    RoleResponse getRoleById(String roleId);

}
