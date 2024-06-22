package com.review.monkey.security.mapper;

import com.review.monkey.security.model.mapping.RolePermission;
import com.review.monkey.security.request.RolePermissionRequest;
import com.review.monkey.security.response.RolePermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    @Mapping (source = "role.roleId" , target = "roleId")
    @Mapping (source = "permission.permissionId" , target =  "permissionId")
    RolePermissionResponse toRolePermissionResponse (RolePermission rolePermission);
}
