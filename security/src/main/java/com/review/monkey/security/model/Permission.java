package com.review.monkey.security.model;

import com.review.monkey.security.model.mapping.RolePermission;
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
@Entity(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "permission_id")
    String permissionId;

    @Column(name = "permission_name")
    String permissionName;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "permission")
    List<RolePermission> rolePermissions = new ArrayList<>();
}
