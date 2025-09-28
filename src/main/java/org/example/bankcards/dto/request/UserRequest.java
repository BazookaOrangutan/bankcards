package org.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Schema(description = "Номер телефона", example = "+79606006060")
    private String phone;

    @Email(message = "The email address must be in the format user@example.com")
    @NotBlank(message = "Email is required")
    @Schema(description = "email пользователя", example = "email@mail.ru")
    private String email;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Owner name too long")
    @Schema(description = "ФИО пользователя", example = "Иванов Иван Иванович")
    private String name;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Новый пароль пользователя", example = "secret_pass1")
    private String password;

}
