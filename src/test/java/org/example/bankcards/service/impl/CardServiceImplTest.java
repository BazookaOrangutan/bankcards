package org.example.bankcards.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.bankcards.dto.request.CardRequest;
import org.example.bankcards.dto.response.CardResponse;
import org.example.bankcards.entity.Card;
import org.example.bankcards.entity.CardStatus;
import org.example.bankcards.entity.Role;
import org.example.bankcards.entity.User;
import org.example.bankcards.mapper.CardMapper;
import org.example.bankcards.repository.CardRepository;
import org.example.bankcards.repository.UserRepository;
import org.example.bankcards.security.UserDetailsImpl;
import org.example.bankcards.util.CardEncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CardEncryptionUtil encryptionUtil;
    @Mock
    private CardEncryptionUtil cardEncryptionUtil;

    @InjectMocks
    private CardServiceImpl cardService;

    private User user;
    private Card card;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .userDetails(UserDetailsImpl.builder().role(Role.ROLE_USER).build())
                .cards(List.of())
                .build();

        card = Card.builder()
                .id(UUID.randomUUID())
                .cardNumber("encrypted")
                .cardNumberHash("hash")
                .expiryDate(LocalDate.now().plusYears(1))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(100))
                .user(user)
                .build();
    }

    @Test
    void shouldThrowIfUserIsAdmin() {

        // GIVEN
        user.getUserDetails().setRole(Role.ROLE_ADMIN);
        CardRequest req = new CardRequest();
        req.setUserId(user.getId());
        req.setExpiryDate(LocalDate.now().plusYears(1));
        req.setCardNumber("1234567812345678");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // WHEN + THEN
        assertThrows(AccessDeniedException.class, () -> cardService.createCard(req));
    }

    @Test
    void shouldThrowIfExpiryDateInPast() {

        // GIVEN
        CardRequest req = new CardRequest();
        req.setUserId(user.getId());
        req.setExpiryDate(LocalDate.now().minusDays(1));
        req.setCardNumber("1234567812345678");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // WHEN + THEN
        assertThrows(IllegalArgumentException.class, () -> cardService.createCard(req));
    }

    @Test
    void shouldChangeStatusSuccessfully() {

        // GIVEN
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
        given(cardRepository.save(any())).willReturn(card);
        given(cardMapper.toResponse(any())).willReturn(new CardResponse());

        // WHEN
        CardResponse result = cardService.changeStatus(card.getId(), CardStatus.BLOCKED);

        // THEN
        assertNotNull(result);
        verify(cardRepository).save(any());
    }

    @Test
    void shouldThrowIfCardAlreadyHasStatus() {

        // GIVEN
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));

        // WHEN + THEN
        assertThrows(IllegalStateException.class,
                () -> cardService.changeStatus(card.getId(), CardStatus.ACTIVE));
    }

    @Test
    void shouldDeleteCardIfExists() {

        // GIVEN
        given(cardRepository.existsById(card.getId())).willReturn(true);

        // WHEN
        cardService.deleteCard(card.getId());

        // THEN
        verify(cardRepository).deleteById(card.getId());
    }

    @Test
    void shouldThrowIfCardNotFoundOnDelete() {

        // GIVEN
        given(cardRepository.existsById(card.getId())).willReturn(false);

        // WHEN + THEN
        assertThrows(EntityNotFoundException.class,
                () -> cardService.deleteCard(card.getId()));
    }

}

