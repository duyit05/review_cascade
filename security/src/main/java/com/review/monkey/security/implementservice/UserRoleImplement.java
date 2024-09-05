package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.UserRoleMapper;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.repository.RoleRepository;
import com.review.monkey.security.repository.UserRepository;
import com.review.monkey.security.repository.UserRoleRepository;
import com.review.monkey.security.response.UserRoleResponse;
import com.review.monkey.security.service.UserRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserRoleImplement implements UserRoleService {

    UserRoleRepository userRoleRepository;
    UserRoleMapper userRoleMapper;
    RoleRepository roleRepository;
    UserRepository userRepository;

    @Override
    public List<UserRoleResponse> getAllUserAndRole() {
       return null;
    }

    @Override
    public UserRoleResponse createUserRole(String userId, String roleId) {
        Optional<UserRole> checkExist = userRoleRepository.findByUserAndRole(userId, roleId);
        if (checkExist.isPresent()) {
            throw new AppException(ErrorCode.USER_ID_AND_ROLE_ID_EXISTED);
        }
        User user = User.builder().userId(userId).build();
        Role role = Role.builder().roleId(roleId).build();

        UserRole userRole = UserRole.builder().
                user(user)
                .role(role)
                .build();

        userRoleRepository.save(userRole);

        return UserRoleResponse.builder().userId(userId).roleId(roleId).build();
    }

    @Override
    public UserRoleResponse updateUserRole(int userRoleId, String roleId) {
        Optional<UserRole> userRoleExsit = userRoleRepository.findById(userRoleId);
        if (userRoleExsit.isPresent()) {
            UserRole userRole = userRoleExsit.get();

            Optional<UserRole> check = userRoleRepository.findByUserAndRoleAnUserRoleId(userRole.getUser().getUserId(), roleId, userRoleId);
            if (check.isPresent()) {
                throw new AppException(ErrorCode.USER_ID_AND_ROLE_ID_EXISTED);
            }
            Role role = new Role();
            role.setRoleId(roleId);
            userRole.setRole(role);

            return userRoleMapper.toUserRoleResponse(userRoleRepository.save(userRole));
        }
        return null;
    }

    @Override
    public void deleteUserRoleById(int userRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId).orElseThrow(() -> new RuntimeException("CAN'T DELETE USER_ROLE WITH ID : " + userRoleId));
        userRoleRepository.delete(userRole);
    }
    
}
