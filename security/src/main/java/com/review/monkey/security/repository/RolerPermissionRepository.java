package com.review.monkey.security.repository;

import com.review.monkey.security.model.mapping.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RolerPermissionRepository extends JpaRepository<RolePermission , Integer> {
    @Query("select r from role_permission r  where r.role.roleId = ?1 and r.permission.permissionId = ?2")
    Optional<RolePermission> findByRoleAndPermission (int roleid , int permissionId);

    @Query("select r from role_permission r where r.role.roleId = ?1 and r.permission.permissionId = ?2 and r.rolePermissionId <> ?3")
    Optional<RolePermission> findRoleAndPermissionAndRolePermissionId (int rolePermissionId , int roleId , int permissionId);
}
