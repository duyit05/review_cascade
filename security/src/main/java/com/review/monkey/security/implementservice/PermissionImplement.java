package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.PermissionMapper;
import com.review.monkey.security.model.Permission;
import com.review.monkey.security.repository.PermissionRepository;
import com.review.monkey.security.request.PermissionRequest;
import com.review.monkey.security.response.PermissionResponse;
import com.review.monkey.security.service.PermissionSerivce;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionImplement implements PermissionSerivce {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public List<PermissionResponse> getAllPermission() {
        List<PermissionResponse> permissionResponseList = new ArrayList<>();
        List<Permission> list = permissionRepository.findAll();
        for (Permission permission : list) {

            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setPermissionId(permission.getPermissionId());
            permissionResponse.setPermissionName(permission.getPermissionName());
            permissionResponse.setDescription(permission.getDescription());

            permissionResponseList.add(permissionResponse);
        }
        return permissionResponseList;
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByPermissionName(request.getPermissionName()))
            throw new AppException(ErrorCode.PERMISSON_NAME_EXISTED);
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPemissionResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse updatePermissionById(int permissionId, PermissionRequest request) {
        Permission checkExisted = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("CAN'T UPDATE PERMISSION WITH ID : " + permissionId));
        permissionMapper.update(checkExisted, request);
        return permissionMapper.toPemissionResponse(permissionRepository.save(checkExisted));
    }

    @Override
    public void deletePermissionById(int permissionId) {
        Permission checkExisted = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("CAN'T DELETE PERMISSION WITH ID : " + permissionId));
        permissionRepository.delete(checkExisted);
    }

    @Override
    public PermissionResponse getPermissionById(int permissionId) {
        return permissionMapper.toPemissionResponse(permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("CAN'T DELETE PERMISSION WITH ID : " + permissionId)));
    }
}
