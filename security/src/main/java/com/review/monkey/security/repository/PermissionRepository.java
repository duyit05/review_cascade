package com.review.monkey.security.repository;


import com.review.monkey.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission , String> {
    boolean existsByPermissionName (String permissionName);
}
