package com.review.monkey.security.model;

import com.review.monkey.security.model.mapping.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    String userId;

    @Column(name= "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "dob")
    LocalDate dob;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<UserRole> userRoles = new ArrayList<>();

}
