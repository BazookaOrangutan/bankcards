package org.example.bankcards.util.converter;

import org.example.bankcards.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class RoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        String clean = source.trim().toUpperCase();
        if (clean.startsWith("ROLE_")) {
            clean = clean.substring(5);
        }
        return Role.valueOf(clean);
    }
}