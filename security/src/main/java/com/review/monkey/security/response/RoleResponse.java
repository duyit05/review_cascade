package com.review.monkey.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String roleId;
    String roleName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String description;
}
