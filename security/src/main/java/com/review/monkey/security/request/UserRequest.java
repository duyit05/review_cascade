package com.review.monkey.security.request;


import com.review.monkey.security.validator.DobConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequest {

    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    //@Email (message = "Format email not true")
    String firstName;
    String lastName;

    @DobConstraint(min = 16 , message = "DOB_INVALID")
    LocalDate dob;
}
