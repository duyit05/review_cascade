package com.review.monkey.security.exception;

import com.review.monkey.security.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final ApiResponse response = new ApiResponse();

    private static final String MIN_ATTRIBUTE = "min";

    // EXCEPTION FOR RuntimeException HAPPEN NOT EXPECT
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception : ", exception);
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


    // EXCEPTION FOR Appexception USING FOR PACKAGE IMPLEMENT
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);

    }

    // EXCEPTION FOR MethodArgumentNotValidException WHEN USING @Validation
    //  USING CHANGE REQUEST WHEN CLASS REQUEST HAVE CHANGE, SUCH AS WHEN CHANGE AGE
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.KEY_INVALID;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();

            if (!allErrors.isEmpty()) {
                ConstraintViolation<?> constraintViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

                attributes = constraintViolation.getConstraintDescriptor().getAttributes();

                log.info(attributes.toString());
            } else {
                log.warn("No errors found in BindingResult.");
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid enumKey provided: " + enumKey, e);
        }
        response.setCode(errorCode.getCode());

        response.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE)) ;
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    // EXCEPTION FOR JWT
    @ExceptionHandler(value = ParseException.class)
    ResponseEntity<ApiResponse> handlingParseException(ParseException exception) {
        response.setCode(4000);
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // EXCEPTION FOR ACCESSDENIED
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
