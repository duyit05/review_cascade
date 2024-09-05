package com.review.monkey.security.controller;


import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.UserRoleResponse;

import com.review.monkey.security.service.UserRoleService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user-role")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class UserRoleController {

    UserRoleService userRoleService;

    @GetMapping
    public ApiResponse<List<UserRoleResponse>> getAllUserAndRole (){
        return ApiResponse.<List<UserRoleResponse>>builder()
                .result(userRoleService.getAllUserAndRole())
                .build();
    }

    @PostMapping
    public ApiResponse<UserRoleResponse> createUserAndRole(@RequestParam String userId , @RequestParam String roleId) {
        return ApiResponse.<UserRoleResponse>builder()
                .result(userRoleService.createUserRole(userId , roleId))
                .build();
    }

    @PutMapping
    public ApiResponse<UserRoleResponse> updateUserAndRole (@RequestParam int userRoleId , @RequestParam String roleId){
       return ApiResponse.<UserRoleResponse>builder()
               .result(userRoleService.updateUserRole(userRoleId , roleId))
               .build();
    }

    @DeleteMapping("/{userRoleId}")
    public String deleteUserAndRoleById (@PathVariable int userRoleId){
        userRoleService.deleteUserRoleById(userRoleId);
        return ("USER_ROLE WITH ID : "  + " " + userRoleId +  " " + " HAS BEEN DELETED");
    }


}
