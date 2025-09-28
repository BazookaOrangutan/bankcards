package org.example.bankcards.security;

import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.request.SignInRequest;
import org.example.bankcards.dto.request.SignUpRequest;
import org.example.bankcards.dto.response.JwtAuthenticationResponse;
import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.example.bankcards.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var userDetails = UserDetailsImpl.builder()
                .username(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
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

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPhone(),
                request.getPassword()
        ));

        var userDetails = userDetailsService.userDetailsService()
                .loadUserByUsername(request.getPhone());

        var jwt = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }
}
