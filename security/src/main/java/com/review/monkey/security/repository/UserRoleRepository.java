package com.review.monkey.security.repository;


import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.request.UserRoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    // PARAM
    @Query("select u from user_role u where u.user.userId = ?1 and u.role.roleId = ?2")
    Optional<UserRole> findByUserAndRole(int userId  , int roleId);

    //  Object
    // @Query("select u from UserRole u where u.user.userId = :#{#request.userId} and u.role.roleId = :#{#request.roleId}")
    //  Optional<UserRole> findByUserAndRole(UserRoleRequest request);

    @Query("select u from user_role u where u.user.userId = ?1 and u.role.roleId = ?2 and u.userRoleId <> ?3 ")
    Optional<UserRole> findByUserAndRoleAnUserRoleId (int userId , int roleId , int userRoleId);
}
