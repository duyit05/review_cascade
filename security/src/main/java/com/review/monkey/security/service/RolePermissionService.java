package com.review.monkey.security.service;

import com.review.monkey.security.response.RolePermissionResponse;
import org.springframework.stereotype.Service;

@Service
public interface RolePermissionService {
    RolePermissionResponse createRoleAndPermission (String roleId , String permissionId);
    RolePermissionResponse updateRoleAndPermission (int rolePermssionId , String permissionId);
    void deleteroleAndPermissionById (int rolePermissionId);
}
