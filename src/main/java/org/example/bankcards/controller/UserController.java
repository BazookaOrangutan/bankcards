package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.filter.UserFilter;
import org.example.bankcards.dto.request.UserRequest;
import org.example.bankcards.dto.response.UserResponse;
import org.example.bankcards.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Контроллер для управления пользователями. Принимает только токены админа")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех пользователей")
    public Page<UserResponse> getAllUsers(
            @Parameter(description = "Набор фильтров для пользователей") UserFilter userFilter,
            @Parameter(description = "Настройки Pageable") Pageable pageable) {
        return userService.getAllUsers(userFilter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение пользователя по id")
    public UserResponse getUserById(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true) @PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление данных пользователя")
    public UserResponse updateUser(
            @Parameter(description = "Уникальный идентификатор обновляемого пользователя", required = true)
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные о пользователе для обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ) @RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление пользователя")
    public void deleteUser(
            @Parameter(description = "Уникальный идентификатор удаляемого пользователя", required = true
            ) @PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
