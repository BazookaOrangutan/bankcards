package org.example.bankcards.util.filter;

import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> role == null
                ? cb.conjunction() : cb.equal(root.get("userDetails").get("role"), role);
    }
}
