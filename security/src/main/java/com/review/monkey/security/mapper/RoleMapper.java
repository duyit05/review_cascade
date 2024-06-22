package com.review.monkey.security.mapper;


import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.request.RoleRequest;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.response.RoleResponse;
import com.review.monkey.security.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole (RoleRequest request);
    RoleResponse toRoleResponse (Role role);
    void updateRole (@MappingTarget Role role , RoleRequest request);
}
