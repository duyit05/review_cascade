package com.review.monkey.security.model;

import com.review.monkey.security.model.mapping.RolePermission;
import com.review.monkey.security.model.mapping.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    String roleId;

    @Column(name = "role_name")
    String roleName;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "role" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<UserRole> roleUsers = new ArrayList<>();

    @OneToMany(mappedBy = "role" , fetch = FetchType.LAZY , cascade =  CascadeType.ALL)
    List<RolePermission> rolePermission = new ArrayList<>();
}
