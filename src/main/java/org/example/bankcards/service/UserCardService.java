package org.example.bankcards.service;

import org.example.bankcards.dto.filter.CardFilter;
import org.example.bankcards.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.InsufficientResourcesException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * Сервис для управления своими картами обычным пользователем.
 */
public interface UserCardService {

    /**
     * Получает список карт, принадлежащих текущему аутентифицированному пользователю,
     * с возможностью фильтрации и постраничной навигации.
     *
     * @param cardFilter параметры фильтрации карт
     * @param pageable   параметры пагинации
     * @return страница с карточками в формате {@link CardResponse}
     */
    Page<CardResponse> getAllMyCards(CardFilter cardFilter, Pageable pageable);

    /**
     * Осуществляет перевод денежных средств с одной карты на другую.
     *
     * @param sourceCardId уникальный идентификатор исходной карты
     * @param targetCardId уникальный идентификатор целевой карты
     * @param amount       сумма перевода
     */
    void transferMoney(UUID sourceCardId, UUID targetCardId, double amount)
            throws InsufficientResourcesException, AccessDeniedException;

    /**
     * Отправляет запрос на блокировку карты от имени пользователя.
     *
     * @param cardId уникальный идентификатор карты
     * @return обновлённая карта в формате {@link CardResponse}
     */
    CardResponse blockRequestFromUser(UUID cardId) throws AccessDeniedException;

    /**
     * Получает текущий баланс карты.
     *
     * @param cardId уникальный идентификатор карты
     * @return баланс карты
     */
    double getBalance(UUID cardId) throws AccessDeniedException;
}
