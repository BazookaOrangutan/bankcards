package org.example.bankcards.security;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.repository.UserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public UserDetails getByUsername(String username) {

        return userDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public void canBeCreated(UserDetailsImpl user) {

        if (userDetailsRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("Username already exists");
        }

        if (userDetailsRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }

    }
}
