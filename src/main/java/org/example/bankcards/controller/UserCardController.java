package org.example.bankcards.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.filter.CardFilter;
import org.example.bankcards.dto.request.TransferRequest;
import org.example.bankcards.dto.response.CardResponse;
import org.example.bankcards.service.UserCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "User Card Controller", description = "Контроллер юзера для управления принадлежащими ему картами. Принимает только токены юзера")
public class UserCardController {

    private final UserCardService userCardService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получение всех своих карт пользователем")
    public Page<CardResponse> getAllMyCards(
            @Parameter(description = "Набор фильтров для карт") CardFilter cardFilter,
            @Parameter(description = "Настройки Pageable") Pageable pageable) {
        return userCardService.getAllMyCards(cardFilter, pageable);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Перевод между своими картами")
    public void transferMoney(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для перевода денег",
            required = true,
            content = @Content(schema = @Schema(implementation = TransferRequest.class))
    ) @RequestBody @Valid TransferRequest request)
            throws InsufficientResourcesException, AccessDeniedException {
        userCardService.transferMoney(request.getSourceCardId(), request.getTargetCardId(), request.getAmount());
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Запрос на блокировку своей карты")
    public CardResponse blockRequestFromUser(
            @Parameter(description = "Уникальнеый идентификатор карты для блокировки", required = true)
            @PathVariable UUID id) throws AccessDeniedException {
        return userCardService.blockRequestFromUser(id);
    }

    @GetMapping("/{id}/balance")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получение баланса карты")
    public Double getBalance(@Parameter(description = "Уникалльный идентификатор карты", required = true)
                             @PathVariable UUID id) throws AccessDeniedException {
        return userCardService.getBalance(id);
    }

}