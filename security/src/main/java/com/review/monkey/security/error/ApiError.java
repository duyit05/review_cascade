package com.review.monkey.security.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level =  AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonFormat(pattern = "dd/MM/yyy hh:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
    int code;
    String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ApiSubError> apiSubErrors = new ArrayList<>();

    public ApiError (String message){
        this.message = message;
    }

    public ApiError (int code){
        this.code = code;
    }

    public ApiError (String message , int code) {
        this(message);
        this.code = code;
    }

    private void addSubError (ApiSubError subError){
        if(apiSubErrors == null){
            apiSubErrors = new ArrayList<>();
        }
        apiSubErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError<>(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError<>(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }
}
