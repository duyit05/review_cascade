package com.review.monkey.security.mapper;

import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.request.UserRoleRequest;
import com.review.monkey.security.response.UserRoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    UserRoleResponse toUserRoleResponse (UserRole userRole);
}
