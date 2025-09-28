package org.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CardRequest {

    @NotBlank(message = "Card number is required")
    @Schema(description = "Номер карты", example = "1234123412341234")
    private String cardNumber;

    @NotNull(message = "Expiry date is required")
    @Schema(description = "Срок действия карты", example = "2030-03-25")
    private LocalDate expiryDate;

    @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
    @Schema(description = "Баланс карты", example = "0")
    private BigDecimal balance;

    @NotNull(message = "Owner is required")
    @Schema(description = "Id владельца", example = "5c87ad40-ca0e-42a7-ae86-c406e776f81e")
    private UUID userId;
}
