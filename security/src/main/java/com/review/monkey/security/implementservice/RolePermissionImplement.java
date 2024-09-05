package com.review.monkey.security.implementservice;

import com.review.monkey.security.mapper.RolePermissionMapper;
import com.review.monkey.security.model.Permission;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.mapping.RolePermission;
import com.review.monkey.security.repository.RolerPermissionRepository;
import com.review.monkey.security.response.RolePermissionResponse;
import com.review.monkey.security.service.RolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolePermissionImplement implements RolePermissionService {

    RolerPermissionRepository rolePermissionRepository;
    RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermissionResponse createRoleAndPermission(String roleId, String permissionId) {
        Optional<RolePermission> checkExisted = rolePermissionRepository.findByRoleAndPermission(roleId, permissionId);
        if (checkExisted.isPresent()) {
            throw new RuntimeException("THIS ROLE HAVED PERMISSION : " + checkExisted.get().getPermission().getPermissionName());
        }
        Role role = Role.builder().roleId(roleId).build();
        Permission permission = Permission.builder().permissionId(permissionId).build();

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();
        RolePermission savedRolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toRolePermissionResponse(savedRolePermission);
    }

    @Override
    public RolePermissionResponse updateRoleAndPermission(int rolePermssionId, String permissionId) {
        Optional<RolePermission> checkExisted = rolePermissionRepository.findById(rolePermssionId);
        if (checkExisted.isPresent()) {
            RolePermission rolePermission = checkExisted.get();

            Optional<RolePermission> check = rolePermissionRepository.findRoleAndPermissionAndRolePermissionId(rolePermssionId, rolePermission.getRole().getRoleId(), permissionId);

            if (check.isPresent())
                throw new RuntimeException("THIS ROLE HAVED PERMISSION : " + checkExisted.get().getPermission().getPermissionName());

            Permission permission = Permission.builder().permissionId(permissionId).build();
            rolePermission.setPermission(permission);

            return rolePermissionMapper.toRolePermissionResponse(rolePermissionRepository.save(rolePermission));
        }
        return null;
    }

    @Override
    public void deleteroleAndPermissionById(int rolePermissionId) {
        RolePermission checkExisted = rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(()-> new RuntimeException("CAN'T FOUND ID : " + rolePermissionId + " " + " TO DELETE"));

        rolePermissionRepository.delete(checkExisted);
    }
}
