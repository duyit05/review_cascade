package com.review.monkey.security.controller;

import com.review.monkey.security.model.mapping.RolePermission;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.RolePermissionResponse;
import com.review.monkey.security.service.RolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/role-permission")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RolePermissionController {
    RolePermissionService rolePermissionService;

    @PostMapping
    public ApiResponse<RolePermissionResponse> createRoleAndPermission (@RequestParam String roleId , @RequestParam String permissionId){
        return ApiResponse.<RolePermissionResponse>builder()
                .result(rolePermissionService.createRoleAndPermission(roleId , permissionId))
                .build();
    }

    @PutMapping
    public ApiResponse<RolePermissionResponse> updateRoleAndPermissionByRolePermissionId (@RequestParam int rolePermissionId , @RequestParam String permissionId ){
        return ApiResponse.<RolePermissionResponse>builder()
                .result(rolePermissionService.updateRoleAndPermission(rolePermissionId , permissionId))
                .build();
    }

    @DeleteMapping("/{rolePermissId}")
    public String deleteRoleAndPermissionById (@PathVariable int rolePermissId ){
        rolePermissionService.deleteroleAndPermissionById(rolePermissId);
        return ("DELETE WITH ID : " + " " + rolePermissId + " " +  " HAS BEEN DELETED");
    }
}
