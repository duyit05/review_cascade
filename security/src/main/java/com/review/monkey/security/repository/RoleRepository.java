package com.review.monkey.security.repository;

import com.review.monkey.security.model.Role;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role , String> {
    boolean existsByRoleName (String roleName);
    Optional<Role> findByRoleName (String roleName);
}
