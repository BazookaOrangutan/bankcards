package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.filter.CardFilter;
import org.example.bankcards.dto.request.CardRequest;
import org.example.bankcards.dto.response.CardResponse;
import org.example.bankcards.entity.CardStatus;
import org.example.bankcards.service.AdminCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin/cards")
@RequiredArgsConstructor
@Tag(name = "Admin Card Controller", description = "Контроллер админа для управления картами. Принимает только токены админа")
public class AdminCardController {

    private final AdminCardService adminCardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех карт", description = "Позволяет получить карты, указав фильтры")
    public Page<CardResponse> getAllCards(@Parameter(description = "Набор фильтров для карт") CardFilter cardFilter,
                                          @Parameter(description = "Настройки Pageable") Pageable pageable) {
        return adminCardService.getAllCards(cardFilter, pageable);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение карты по id")
    public CardResponse getCardById(@Parameter(description = "Уникальный идентификатор карты") @PathVariable UUID id) {
        return adminCardService.getCardById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание карты")
    public CardResponse createCard(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные о карте для создания",
            required = true,
            content = @Content(schema = @Schema(implementation = CardRequest.class)))
                                   @RequestBody @Valid CardRequest cardRequest) throws AccessDeniedException {
        return adminCardService.createCard(cardRequest);
    }

    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменение статуса карты")
    public CardResponse changeStatus(
            @Parameter(description = "Уникальный идентификатор существующей карты") @PathVariable UUID id,
            @Parameter(description = "Новый статус карты") @PathVariable CardStatus status) {
        return adminCardService.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление карты по ее id")
    public void deleteCard(@Parameter(description = "Уникальный идентификатор карты") @PathVariable UUID id) {
        adminCardService.deleteCard(id);
    }

}
