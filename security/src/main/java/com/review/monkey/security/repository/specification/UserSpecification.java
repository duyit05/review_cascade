package com.review.monkey.security.repository.specification;

import com.review.monkey.security.model.User;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSpecification {

    private static final String FIELD_LAST_NAME = "lastName";

    private final List<Specification<User>> specifications = new ArrayList<>();

    public static UserSpecification builder() {
        return new UserSpecification();
    }

    public UserSpecification withLastName(String lastName) {
        if(!ObjectUtils.isEmpty(lastName)) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(FIELD_LAST_NAME)), like(lastName))
            );
        }
        return this;
    }

    public static String like(String value) {
        return "%" + value.toUpperCase() + "%";
    }


    public Specification<User> build() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.and(specifications.stream()
                        .filter(Objects::nonNull)
                        .map(s -> s.toPredicate(root, query, criteriaBuilder))
                        .toArray(Predicate[]::new)));
    }
}
