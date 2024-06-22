package com.review.monkey.security.controller;

import com.review.monkey.security.request.RoleRequest;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.RoleResponse;
import com.review.monkey.security.service.RoleService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.PrimitiveIterator;

@RequestMapping("/role")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RoleController {

    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole (){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> createRole (@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @DeleteMapping("/{roleId}")
    public String deleteRole (@PathVariable int roleId){
        roleService.deleteRoleById(roleId);
        return  ("ROLE WITH ID :  " + roleId + "HAS BEEN DELETED") ;
    }

    @PutMapping("/{roleId}")
    public ApiResponse<RoleResponse> getAllRole (@PathVariable int roleId ,  @RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRoleById(roleId, request))
                .build();
    }

    @GetMapping("/{roleId}")
    public ApiResponse<RoleResponse> getAllRole (@PathVariable int roleId) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRoleById(roleId))
                .build();
    }
}
