package com.review.monkey.security.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException  extends RuntimeException {
    ErrorCode errorCode;
    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
