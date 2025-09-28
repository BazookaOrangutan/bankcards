package org.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {

    private UUID id;

    private String cardNumber;

    private LocalDate expiryDate;

    private CardStatus status;

    private BigDecimal balance;

    private UUID userId;
}
