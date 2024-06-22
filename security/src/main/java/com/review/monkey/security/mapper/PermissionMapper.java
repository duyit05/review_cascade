package com.review.monkey.security.mapper;

import com.review.monkey.security.model.Permission;
import com.review.monkey.security.request.PermissionRequest;
import com.review.monkey.security.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel =  "spring")
public interface PermissionMapper {
    Permission toPermission (PermissionRequest request);
    PermissionResponse toPemissionResponse (Permission permission);

    void update (@MappingTarget Permission permission , PermissionRequest request);
}
