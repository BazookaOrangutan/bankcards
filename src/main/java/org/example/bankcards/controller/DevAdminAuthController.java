package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.request.SignUpRequest;
import org.example.bankcards.dto.response.JwtAuthenticationResponse;
import org.example.bankcards.security.DevAdminAuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
@RequestMapping("/api/v1/auth/dev")
@RequiredArgsConstructor
@Tag(name = "Dev Tools", description = "Вспомогательные эндпоинты для разработки и тестирования. НЕ ДЛЯ ПРОДАКШЕНА!")
public class DevAdminAuthController {

    private final DevAdminAuthService authService;

    @PutMapping("/sign-up/admin")
    @Operation(summary = "Регистрация админа")
    public JwtAuthenticationResponse SignUpAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для регистрации",
            required = true,
            content = @Content(schema = @Schema(implementation = SignUpRequest.class))
    ) @Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}