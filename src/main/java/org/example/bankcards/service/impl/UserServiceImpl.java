package org.example.bankcards.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.filter.UserFilter;
import org.example.bankcards.dto.request.UserRequest;
import org.example.bankcards.dto.response.UserResponse;
import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.example.bankcards.mapper.UserMapper;
import org.example.bankcards.repository.UserDetailsRepository;
import org.example.bankcards.repository.UserRepository;
import org.example.bankcards.security.UserDetailsImpl;
import org.example.bankcards.service.UserService;
import org.example.bankcards.util.filter.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(UserFilter userFilter, Pageable pageable) {

        Specification<User> specification = Specification.allOf(
                UserSpecification.hasRole(Role.valueOf(userFilter.getRole()))
        );

        Page<User> users = userRepository.findAll(specification, pageable);
        System.out.println(users.get());
        return users.map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserRequest userRequest) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (userRequest.getName() != null) {
            existingUser.setName(userRequest.getName());
        }

        UserDetailsImpl userDetails = existingUser.getUserDetails();

        if (userDetails == null) {
            throw new IllegalStateException("User details not found for user: " + id);
        }

        if (userRequest.getPhone() != null && !userRequest.getPhone().equals(userDetails.getUsername())) {
            if (userDetailsRepository.existsByUsername(userRequest.getPhone())) {
                throw new EntityExistsException("Phone number already taken");
            }
            userDetails.setUsername(userRequest.getPhone());
        }

        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(userDetails.getEmail())) {
            if (userDetailsRepository.existsByEmail(userRequest.getEmail())) {
                throw new EntityExistsException("Email already taken: " + userRequest.getEmail());
            }
            userDetails.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            userDetails.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}