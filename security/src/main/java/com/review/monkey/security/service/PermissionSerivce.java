package com.review.monkey.security.service;

import com.review.monkey.security.request.PermissionRequest;
import com.review.monkey.security.response.PermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionSerivce {
    List<PermissionResponse> getAllPermission();

    PermissionResponse createPermission(PermissionRequest request);

    PermissionResponse updatePermissionById(int permissionId, PermissionRequest request);

    void deletePermissionById(int permissionId);

    PermissionResponse getPermissionById(int permissionId);
}
