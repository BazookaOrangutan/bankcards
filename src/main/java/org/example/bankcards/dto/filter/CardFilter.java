package org.example.bankcards.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.bankcards.entity.CardStatus;

import java.util.UUID;

@Data
public class CardFilter {

    @Schema(description = "Статус карты", example = "ACTIVE")
    private CardStatus status;

    @Schema(description = "Id пользователя", example = "329f82e1-6d5a-40d9-91f3-11e5fccd249a")
    private UUID userId;

    @Schema(description = "Больше или равно балансу на карте", example = "1000")
    private double balance;
}