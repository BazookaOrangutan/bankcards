package org.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransferRequest {

    @NotNull(message = "Source card id is required")
    @Schema(description = "id карты списания", example = "5c87ad40-ca0e-42a7-ae86-c406e776f81e")
    private UUID sourceCardId;

    @NotNull(message = "Target card id is required")
    @Schema(description = "id карты зачисления", example = "dc6070b2-f478-493b-8fb7-91dcedbb7a25")
    private UUID targetCardId;

    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Schema(description = "Сумма перевода", example = "5000")
    private double amount;
}
