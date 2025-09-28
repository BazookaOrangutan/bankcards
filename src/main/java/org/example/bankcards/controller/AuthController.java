package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.request.SignInRequest;
import org.example.bankcards.dto.request.SignUpRequest;
import org.example.bankcards.dto.response.JwtAuthenticationResponse;
import org.example.bankcards.security.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignUpRequest.class))
            ) @RequestBody @Valid SignUpRequest request) {
        return authService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для входа",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignInRequest.class))
            ) @RequestBody @Valid SignInRequest request) {
        return authService.signIn(request);
    }
}
