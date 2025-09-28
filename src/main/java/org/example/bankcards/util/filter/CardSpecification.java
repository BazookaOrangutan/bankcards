package org.example.bankcards.util.filter;

import org.example.bankcards.entity.Card;
import org.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CardSpecification {

    public static Specification<Card> hasStatus(CardStatus status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Card> hasUserId(UUID userId) {
        return (root, query, cb) -> userId == null
                ? cb.conjunction() : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Card> hasGraterOrEqualBalance(double balance) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("balance"), balance);
    }
}
