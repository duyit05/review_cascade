package com.review.monkey.security.exception;

import com.review.monkey.security.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    public static final ApiResponse response = new ApiResponse();

    // EXCEPTION FOR RuntimeException
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        response.setCode(1001);
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    // EXCEPTION FOR Appexception USING FOR PACKAGE IMPLEMENT
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMassage());
        return ResponseEntity.badRequest().body(response);
    }

    // EXCEPTION FOR MethodArgumentNotValidException WHEN USING @Validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.KEY_INVALID;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {

        }
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMassage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = ParseException.class)
    ResponseEntity<ApiResponse> handlingParseException (ParseException exception){
        response.setCode(4000);
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
