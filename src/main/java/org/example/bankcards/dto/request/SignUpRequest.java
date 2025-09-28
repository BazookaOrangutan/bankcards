package org.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Номер телефона", example = "+74214354444")
    @Size(min = 3, max = 16, message = "Номер телефона должен содержать от 3 до 16 символов")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    @Schema(description = "Адрес электронной почты", example = "user@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Email is required")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "ФИО пользователя", example = "Иванов Иван Иванович")
    @NotBlank(message = "Name is required")
    private String name;
}