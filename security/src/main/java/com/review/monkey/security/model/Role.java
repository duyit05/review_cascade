package com.review.monkey.security.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    int roleId;

    @Column(name = "role_name")
    String roleName;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "role")
    List<UserRole> roleUsers = new ArrayList<>();
}
