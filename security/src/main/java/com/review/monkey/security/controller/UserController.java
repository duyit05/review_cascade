package com.review.monkey.security.controller;

import com.review.monkey.security.request.PagingRequest;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.request.UserSearchRequest;
import com.review.monkey.security.request.update.UserUpdateRequest;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.PageableResponse;
import com.review.monkey.security.response.PagingResponse;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.UserDatabase;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.SpinnerUI;
import java.util.List;

@RequestMapping("/user")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username : " + authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        log.info("Controller : Create user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/my-info-or-update")
    public ApiResponse<UserResponse> getMyInfoOrUpdate(@RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfoOrUpdate(request))
                .build();

    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfoView())
                .build();

    }

    @PostMapping("/search-and-sort")
    public ApiResponse<PagingResponse<UserResponse>> getUserSortAndSearch(@RequestBody UserSearchRequest request) {
        final Page<UserResponse> users = userService.getUserPagingAndSearch(request);
        final PagingRequest paging = request.getPaging();
        return ApiResponse.<PagingResponse<UserResponse>>builder()
                .result(new PagingResponse<UserResponse>()
                        .setContents(users.getContent())
                        .setPaing(new PageableResponse()
                                .setPageNumber(paging.getPage() - 1)
                                .setTotalPage(users.getTotalPages())
                                .setPageSize(paging.getSize())
                                .setToltalRecord(users.getTotalElements())
                        )
                ).build();
    }

    @GetMapping("/clean-cache")
    @CacheEvict(value = "user" , allEntries = true)
    public String cleanCache (){
        return "Clean cache successfully";
    }

}
