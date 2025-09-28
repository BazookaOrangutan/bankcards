package org.example.bankcards.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankcards.entity.Role;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID id;

    private String username;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private List<UUID> cardIds;

}
