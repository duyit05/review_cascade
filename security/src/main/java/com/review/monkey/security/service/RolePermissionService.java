package com.review.monkey.security.service;

import com.review.monkey.security.response.RolePermissionResponse;
import org.springframework.stereotype.Service;

@Service
public interface RolePermissionService {
    RolePermissionResponse createRoleAndPermission (int roleId , int permissionId);
    RolePermissionResponse updateRoleAndPermission (int rolePermssionId , int permissionId);
    void deleteroleAndPermissionById (int rolePermissionId);
}
