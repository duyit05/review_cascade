package com.review.monkey.security.mapper;

import com.review.monkey.security.model.User;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface UserMapper {
    User toUser (UserRequest request);
    UserResponse toUserResponse (User user);

    void updateUser (@MappingTarget User user , UserRequest request);
}
