package com.review.monkey.security.config;

import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.repository.RoleRepository;
import com.review.monkey.security.repository.UserRepository;
import com.review.monkey.security.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ApplicationConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;

    // WHEN THE FIRST START APPLICATION THIS CLASS WILL START
    @Bean

    // CONDITION FOR DATABASE H2 WHEN USING UNIT TEST
    @ConditionalOnProperty(prefix = "spring",
                           value = "spring.datasource.driverClassName",
                           havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner applicationRunner(UserRepository userRepository) {

        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                Role checkRoleExisted = roleRepository.findById("38052e9d-3489-4789-a460-58281ed5ffb4").orElseThrow(() -> new RuntimeException("Cannot find role"));

                if (checkRoleExisted == null) {
                    Role role = Role.builder()
                            .roleName("ADMIN")
                            .description("Admin Role")
                            .build();
                    roleRepository.save(role);
                }

                User userAdmin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("")
                        .lastName("")
                        .build();
                userRepository.save(userAdmin);

                UserRole userRole = UserRole.builder()
                        .user(userAdmin)
                        .role(checkRoleExisted)
                        .build();
                userRoleRepository.save(userRole);
                log.info("user admin has been created with default password please change it");
            }
        };
    }
}
