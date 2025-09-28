package org.example.bankcards.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.bankcards.dto.filter.CardFilter;
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
import org.example.bankcards.service.AdminCardService;
import org.example.bankcards.service.UserCardService;
import org.example.bankcards.util.CardEncryptionUtil;
import org.example.bankcards.util.MaskCardNumberUtil;
import org.example.bankcards.util.filter.CardSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements AdminCardService, UserCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardEncryptionUtil cardEncryptionUtil;


    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAllCards(CardFilter filter, Pageable pageable) {

        Specification<Card> spec = Specification.allOf(
                CardSpecification.hasStatus(filter.getStatus()),
                CardSpecification.hasUserId(filter.getUserId()),
                CardSpecification.hasGraterOrEqualBalance(filter.getBalance()));

        return cardRepository.findAll(spec, pageable).map(card -> cardMapper.toResponse(decryptAndMaskCard(card)));
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse getCardById(UUID id) {
        return cardMapper.toResponse(decryptAndMaskCard(getCardEntityById(id)));
    }

    @Override
    public CardResponse createCard(CardRequest cardRequest) throws AccessDeniedException {

        User user = userRepository.findById(cardRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getUserDetails().getRole() == Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Admin users are not allowed to have bank cards");
        }

        if (cardRequest.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }

        String digitsOnly = validateCardNumber(cardRequest.getCardNumber());

        Card card = Card.builder()
                .cardNumber(cardEncryptionUtil.encrypt(digitsOnly))
                .cardNumberHash(DigestUtils.sha256Hex(digitsOnly))
                .expiryDate(cardRequest.getExpiryDate())
                .status(CardStatus.ACTIVE)
                .balance(cardRequest.getBalance())
                .user(user)
                .build();

        Card savedCard = cardRepository.save(card);

        return cardMapper.toResponse(decryptAndMaskCard(savedCard));
    }

    @Override
    public CardResponse changeStatus(UUID id, CardStatus status) {

        Card card = getCardEntityById(id);

        if (card.getStatus() == status) {
            throw new IllegalStateException("Card is already " + status.name());
        }
        if (card.getExpiryDate().isBefore(LocalDate.now()) && status == CardStatus.ACTIVE) {
            throw new IllegalStateException("Cannot activate expired card");
        }
        card.setStatus(status);

        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Override
    public void deleteCard(UUID cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("Card not found");
        }
        cardRepository.deleteById(cardId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAllMyCards(CardFilter cardFilter, Pageable pageable) {

        UUID userId = getCurrentUserId();

        if (!cardRepository.existsByUserId(userId)) {
            throw new EntityNotFoundException("Cards not found");
        }

        Specification<Card> spec = Specification.allOf(
                CardSpecification.hasStatus(cardFilter.getStatus()),
                CardSpecification.hasUserId(userId),
                CardSpecification.hasGraterOrEqualBalance(cardFilter.getBalance()));

        return cardRepository.findAll(spec, pageable).map(card -> cardMapper.toResponse(decryptAndMaskCard(card)));
    }

    @Override
    public void transferMoney(UUID sourceCardId, UUID targetCardId, double amount)
            throws InsufficientResourcesException, AccessDeniedException {

        ensureCardBelongsToCurrentUser(sourceCardId);
        ensureCardBelongsToCurrentUser(targetCardId);

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Card fromCard = getCardEntityById(sourceCardId);
        Card toCard = getCardEntityById(targetCardId);

        if (!fromCard.getUser().getId().equals(toCard.getUser().getId())) {
            throw new IllegalArgumentException("Cannot transfer between cards of different users");
        }

        if (fromCard.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Source card is not active");
        }
        if (toCard.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Target card is not active");
        }

        if (fromCard.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new InsufficientResourcesException("Insufficient funds on source card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(BigDecimal.valueOf(amount)));
        toCard.setBalance(toCard.getBalance().add(BigDecimal.valueOf(amount)));
    }

    @Override
    public CardResponse blockRequestFromUser(UUID cardId) throws AccessDeniedException {

        ensureCardBelongsToCurrentUser(cardId);

        Card card = getCardEntityById(cardId);
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }
        card.setStatus(CardStatus.BLOCK_REQUESTED);
        Card updatedCard = cardRepository.save(card);

        return cardMapper.toResponse(decryptAndMaskCard(updatedCard));
    }

    @Override
    public double getBalance(UUID cardId) throws AccessDeniedException {

        ensureCardBelongsToCurrentUser(cardId);

        Card card = getCardEntityById(cardId);
        return card.getBalance().doubleValue();
    }

    private Card getCardEntityById(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: " + id));
    }

    private List<CardResponse> getCardsByUserId(UUID userId) {
        return cardRepository.findByUserId(userId).stream()
                .map(card -> cardMapper.toResponse(decryptAndMaskCard(card)))
                .toList();
    }

    private Card decryptAndMaskCard(Card card) {

        Card copy = Card.builder()
                .id(card.getId())
                .cardNumberHash(card.getCardNumberHash())
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .user(card.getUser())
                .build();

        String decrypted = cardEncryptionUtil.decrypt(card.getCardNumber());
        String masked = MaskCardNumberUtil.maskCardNumber(decrypted);
        copy.setCardNumber(masked);

        return copy;
    }

    private UUID getCurrentUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return userRepository.findIdByUserDetailsId(userDetails.getId());
        }
        throw new IllegalStateException("Current user not found");
    }

    private void ensureCardBelongsToCurrentUser(UUID cardId) throws AccessDeniedException {

        UUID currentUserId = getCurrentUserId();
        CardResponse card = getCardsByUserId(currentUserId).stream()
                .filter(c -> c.getId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Card does not belong to current user"));
    }

    private String validateCardNumber(String cardNumber) {

        if (cardNumber == null) {
            throw new IllegalArgumentException("Card number cannot be null");
        }
        String digitsOnly = cardNumber.replaceAll(" ", "");
        if (digitsOnly.length() != 16 || !digitsOnly.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must contain exactly 16 digits");
        }

        return digitsOnly;
    }
}
