package com.review.monkey.security.controller;

import com.review.monkey.security.request.PermissionRequest;
import com.review.monkey.security.response.ApiResponse;
import com.review.monkey.security.response.PermissionResponse;
import com.review.monkey.security.service.PermissionSerivce;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permission")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionSerivce permissionSerivce;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionSerivce.getAllPermission())
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionResponse> getAllPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionSerivce.createPermission(request))
                .build();
    }

    @PutMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> updatePermissionById(@PathVariable String permissionId, @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionSerivce.updatePermissionById(permissionId, request))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    public String deletePermissionById(@PathVariable String permissionId) {
        permissionSerivce.deletePermissionById(permissionId);
        return ("PERMISSION WITH " + " " + permissionId + " " + " HAS BEEN DELETED");
    }

    @GetMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> updatePermissionById(@PathVariable String permissionId) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionSerivce.getPermissionById(permissionId))
                .build();
    }
}
