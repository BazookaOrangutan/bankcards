package org.example.bankcards.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFilter {

    @Schema(description = "Роль пользователя", example = "ROLE_ADMIN")
    private String role;
}
