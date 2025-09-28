package org.example.bankcards.service;

import org.example.bankcards.dto.filter.CardFilter;
import org.example.bankcards.dto.request.CardRequest;
import org.example.bankcards.dto.response.CardResponse;
import org.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * Сервис для управления картами администратором.
 */
public interface AdminCardService {

    /**
     * Получает список всех карт с возможностью фильтрации и постраничной навигации.
     *
     * @param filter   параметры фильтрации карточек
     * @param pageable параметры пагинации
     * @return страница с карточками в формате {@link CardResponse}
     */
    Page<CardResponse> getAllCards(CardFilter filter, Pageable pageable);

    /**
     * Получает карту по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор карточки
     * @return данные карточки в формате {@link CardResponse}
     */
    CardResponse getCardById(UUID id);

    /**
     * Создаёт новую карту.
     *
     * @param cardRequest данные для создания карточки
     * @return созданная карточка в формате {@link CardResponse}
     */
    CardResponse createCard(CardRequest cardRequest) throws AccessDeniedException;

    /**
     * Изменяет статус карты.
     *
     * @param id     уникальный идентификатор карточки
     * @param status новый статус карточки
     * @return обновлённая карточка в формате {@link CardResponse}
     */
    CardResponse changeStatus(UUID id, CardStatus status);

    /**
     * Удаляет карту по её уникальному идентификатору.
     *
     * @param cardId уникальный идентификатор карточки
     */
    void deleteCard(UUID cardId);
}
