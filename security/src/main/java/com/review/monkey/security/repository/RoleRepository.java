package com.review.monkey.security.repository;

import com.review.monkey.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role , Integer> {
    boolean existsByRoleName (String roleName);
}
