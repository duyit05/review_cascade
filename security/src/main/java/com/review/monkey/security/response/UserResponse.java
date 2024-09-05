package com.review.monkey.security.response;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String userId;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    List<RoleResponse> roles;
}
