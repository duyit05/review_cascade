package com.review.monkey.security.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @Size(min = 3, message = "USERNAME_INVALID")
    @JsonIgnore
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    //    @Email (message = "Format email not true")
    String firstName;
    String lastName;
}
