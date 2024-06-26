package com.review.monkey.security.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public enum ErrorCode {
    // THIS DECLADE  (KEY , MASSAGE)
    USER_EXISTED (1002 , "USER EXISTED"),

    // DECLADE AVOID USE NOT CORRECT KEY
    KEY_INVALID (1001 , "INVALID MESSAGE KEY"),


    USERNAME_INVALID (1003 , "USERNAME MUST BE AT LEAST 3 CHARACTERS"),
    PASSWORD_INVALID (1004 , "PASSWORD MUST BE AT LEAST 8 CHARACTERS"),
    USER_NOT_EXIST(1005 , "USER NOT EXISTED"),
    UNAUTHENTICATED(1006 , "UNAUTHENTICATED"),

    // THIS DECLADE ERROR FOR ROLE
    ROLE_NAME_EXISTED(2000 , "ROLE NAME HAS BEEN EXISTED"),

    // THIS DECLADE ERROR FOR USERROLE
    USER_ID_AND_ROLE_ID_EXISTED (3000 , "USER AND ROLE HAS BEEN EXSITED"),

    // THIS DECLADE ERROR FOR PERMISSION
    PERMISSON_NAME_EXISTED (4000 , "PERMISSON HAS BEEN EXISTED"),
    // THIS DECALDE ERROR FOR JWT
    HEADER_INVALID (5000  , "HEADER VALID");
    int code;
    String massage;
}
