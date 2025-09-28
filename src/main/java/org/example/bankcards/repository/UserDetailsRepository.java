package org.example.bankcards.repository;

import org.example.bankcards.security.UserDetailsImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, UUID> {

    Optional<UserDetails> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
