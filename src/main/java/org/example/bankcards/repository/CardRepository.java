package org.example.bankcards.repository;

import org.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

    List<Card> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
