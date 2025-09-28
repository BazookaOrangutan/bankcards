package org.example.bankcards.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.bankcards.security.AuthService;
import org.example.bankcards.security.JwtAuthenticationFilter;
import org.example.bankcards.security.JwtService;
import org.example.bankcards.security.UserDetailsService;
import org.example.bankcards.service.AdminCardService;
import org.example.bankcards.service.UserCardService;
import org.example.bankcards.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public AdminCardService adminCardService() {
        return Mockito.mock(AdminCardService.class);
    }

    @Bean
    public UserCardService userCardService() {
        return Mockito.mock(UserCardService.class);
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }

    @Bean
    public JwtService jwtService() {
        return Mockito.mock(JwtService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return Mockito.mock(UserDetailsService.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return Mockito.mock(JwtAuthenticationFilter.class);
    }
}