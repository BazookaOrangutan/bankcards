package org.example.bankcards.util.filter;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SortValidator {

    private static final Set<String> ALLOWED_CARD_SORT_FIELDS = Set.of("user_id", "expiryDate", "balance", "status");
    private static final Set<String> ALLOWED_USER_SORT_FIELDS = Set.of("id", "username", "name", "email", "role");

    public void validateCardSort(Sort sort) {
        for (Sort.Order order : sort) {
            if (!ALLOWED_CARD_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException("Sorting by '" + order.getProperty() + "' is not allowed");
            }
        }
    }

    public void validateUserSort(Sort sort) {
        for (Sort.Order order : sort) {
            if (!ALLOWED_USER_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException("Sorting by '" + order.getProperty() + "' is not allowed");
            }
        }
    }
}