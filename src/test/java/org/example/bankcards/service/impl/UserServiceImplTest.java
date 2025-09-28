package org.example.bankcards.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.bankcards.dto.request.UserRequest;
import org.example.bankcards.dto.response.UserResponse;
import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.example.bankcards.mapper.UserMapper;
import org.example.bankcards.repository.UserDetailsRepository;
import org.example.bankcards.repository.UserRepository;
import org.example.bankcards.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(UUID.randomUUID())
                .username("+123456789")
                .email("user@test.com")
                .password("pass")
                .role(Role.ROLE_USER)
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .userDetails(userDetails)
                .build();
    }

    @Test
    void shouldGetUserById() {

        // GIVEN
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userMapper.toResponse(user)).willReturn(new UserResponse());

        // WHEN
        UserResponse result = userService.getUserById(user.getId());

        // THEN
        assertNotNull(result);
    }

    @Test
    void shouldThrowIfUserNotFound() {

        // GIVEN
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(user.getId()));
    }

    @Test
    void shouldUpdateUserSuccessfully() {

        // GIVEN
        UserRequest request = new UserRequest();
        request.setName("Updated");
        request.setEmail("new@test.com");
        request.setPhone("+987654321");
        request.setPassword("newPass");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userDetailsRepository.existsByUsername(request.getPhone())).willReturn(false);
        given(userDetailsRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encoded");
        given(userRepository.save(any())).willReturn(user);
        given(userMapper.toResponse(user)).willReturn(new UserResponse());

        // WHEN
        UserResponse result = userService.updateUser(user.getId(), request);

        // THEN
        assertNotNull(result);
        verify(userRepository).save(user);
        assertEquals("Updated", user.getName());
        assertEquals("+987654321", user.getUserDetails().getUsername());
    }

    @Test
    void shouldThrowIfPhoneAlreadyExists() {

        // GIVEN
        UserRequest request = new UserRequest();
        request.setPhone("+987654321");
        request.setEmail("user@test.com");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userDetailsRepository.existsByUsername(request.getPhone())).willReturn(true);

        // WHEN + THEN
        assertThrows(EntityExistsException.class,
                () -> userService.updateUser(user.getId(), request));
    }

    @Test
    void shouldDeleteUserIfExists() {

        // GIVEN
        given(userRepository.existsById(user.getId())).willReturn(true);

        // WHEN
        userService.deleteUser(user.getId());

        // THEN
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void shouldThrowIfUserNotFoundOnDelete() {

        // GIVEN
        given(userRepository.existsById(user.getId())).willReturn(false);

        // WHEN + THEN
        assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(user.getId()));
    }
}
