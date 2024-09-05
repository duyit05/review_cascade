package com.review.monkey.security.service;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.repository.RoleRepository;
import com.review.monkey.security.repository.UserRepository;
import com.review.monkey.security.repository.UserRoleRepository;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    private Role roleDefault;
    private UserRole userRole;

    private UserRequest userRequest;
    private LocalDate dob;
    private User user;

    @BeforeEach
    public void intData() {
        dob = LocalDate.of(1990, 1, 1);
        userRequest = UserRequest.builder()
                .username("john")
                .firstName("john")
                .lastName("john")
                .password("12345678")
                .dob(dob)
                .build();

        user = User.builder()
                .userId("46f0-b7a8")
                .username("john")
                .firstName("john")
                .lastName("john")
                .dob(dob)
                .build();

        roleDefault = Role.builder()
                .roleName("USER")
                .description("User role")
                .build();

        userRole = UserRole.builder()
                .user(user)
                .role(roleDefault)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        // Giả lập quá trình save để trả về đối tượng user với userId mong muốn
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setUserId("46f0-b7a8");  // Gán giá trị userId mong muốn
            return savedUser;
        });

        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(roleDefault));
        when(userRoleRepository.save(any())).thenReturn(userRole);

        // WHEN
        UserResponse response = userService.createUser(userRequest);

        // THEN
        Assertions.assertThat(response.getUserId()).isEqualTo("46f0-b7a8");  // Kiểm tra giá trị mong đợi
        Assertions.assertThat(response.getUsername()).isEqualTo("john");  // Kiểm tra đúng username
    }

    @Test
        // test case for floor service with exception
    void createUser_validRequest_fail() {
        //GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userRequest));
        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    // AVOID AUTHENTICATION WHEN USING JWT
    @WithMockUser(username = "john")
    void getMyinfo_valid_success () {
        //GIVEN
        when(userRepository.findByUsername((anyString()))).thenReturn(Optional.of(user));
        //WHEN
        UserResponse response = userService.getMyInfoView();
        // THEN
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
        Assertions.assertThat(response.getUserId()).isEqualTo("46f0-b7a8");

    }

    @Test
    @WithMockUser(username = "john")
    void getMyinfo_userNotFound_error () {
        //GIVEN
        when(userRepository.findByUsername((anyString()))).thenReturn(Optional.empty());
        //WHEN
        AppException exception = assertThrows(AppException.class , () -> userService.getMyInfoView());
        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);

    }
}
