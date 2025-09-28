package org.example.bankcards.security;

import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.request.SignUpRequest;
import org.example.bankcards.dto.response.JwtAuthenticationResponse;
import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.example.bankcards.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class DevAdminAuthService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var userDetails = UserDetailsImpl.builder()
                .username(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();

        userDetailsService.canBeCreated(userDetails);

        User user = User.builder()
                .name(request.getName())
                .userDetails(userDetails)
                .build();

        userRepository.save(user);

        var jwt = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }
}
