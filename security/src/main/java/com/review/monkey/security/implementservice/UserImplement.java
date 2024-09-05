package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.UserMapper;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.repository.RoleRepository;
import com.review.monkey.security.repository.UserRepository;
import com.review.monkey.security.repository.UserRoleRepository;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.request.UserSearchRequest;
import com.review.monkey.security.request.update.UserUpdateRequest;
import com.review.monkey.security.response.RoleResponse;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserImplement implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;


    @Override
    // CHECK CONDITION FIRST IF CONDITION TRUE THEN ACCESS THIS FUNCTION
    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable("user")
    public List<UserResponse> getAllUser() {
        log.info("THIS IS FUNCTION GET ALL USER");
        List<User> listUser = userRepository.findAll();
        List<UserResponse> userResponse = new ArrayList<>();

        for (User user : listUser) {
            UserResponse userDTO = UserResponse.builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .dob(user.getDob())
                    .build();

            List<RoleResponse> roleResponses = new ArrayList<>();
            for (UserRole userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                RoleResponse roleDTO = RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .build();
                roleResponses.add(roleDTO);
            }

            // Gán danh sách roleResponses cho userDTO
            userDTO.setRoles(roleResponses);
            userResponse.add(userDTO);
        }
        return userResponse;
    }


    @Override
    // ENABLE ACCESS THIS FUNCTION AFTER CHECK CONDITION
    //@PostAuthorize("hasRole('ADMIN')")

    // CHECK CONDITION USERNAME LOGGING  RIGHT WITH ID HAVE USERNAME OR NOT
    // SUCH AS LOGGING WITH USERNAME ARE duy10 THEN GET BY ID OF THIS USERNAME , ANOTHER USERNAME THEN FORBIDENCE
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String userId) {
        log.info("THIS IS FUNCTION GET USER BY ID");
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID : " + userId)));
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        log.info("Create user : Service" );
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .userRoles(new ArrayList<>())
                .build();
        userRepository.save(user);

        Role roleDefault = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NAME_NOT_FOUND));

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(roleDefault)
                .build();
        userRoleRepository.save(userRole);

        user.getUserRoles().add(userRole);

        List<RoleResponse> roleResponses = new ArrayList<>();
        for (UserRole ur : user.getUserRoles()) {
            Role role = ur.getRole();
            RoleResponse roleResponse = RoleResponse.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .description(role.getDescription())
                    .build();
            roleResponses.add(roleResponse);
        }

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .roles(roleResponses)
                .build();
    }


    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("CAN'T DELETE USER WITH ID : " + userId));
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND WITH ID : " + userId));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfoOrUpdate(UserUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USERNAME_INVALID));
        userMapper.updateUserInfo(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfoView() {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(authentication).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> getUserPagingAndSearch(UserSearchRequest request) {
        Page<User> users = userRepository.findAll(request.specification(), request.getPaging().pageable());

        // Convert Page<User> to Page<UserResponse> including role information
        Page<UserResponse> userResponses = users.map(user -> {
            // Build the UserResponse
            UserResponse userResponse = UserResponse.builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername()) // Ensure username is included
                    .dob(user.getDob())
                    .build();

            // Convert UserRoles to RoleResponses
            List<RoleResponse> roleResponses = new ArrayList<>();
            for (UserRole userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                RoleResponse roleResponse = RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .build();
                roleResponses.add(roleResponse);
            }

            // Set the roles in the UserResponse
            userResponse.setRoles(roleResponses);

            return userResponse;
        });

        return userResponses;
    }

}
