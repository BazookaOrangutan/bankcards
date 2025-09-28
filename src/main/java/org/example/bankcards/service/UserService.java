package org.example.bankcards.service;

import org.example.bankcards.dto.filter.UserFilter;
import org.example.bankcards.dto.request.UserRequest;
import org.example.bankcards.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сервис для управления пользователями (административные операции).
 */
public interface UserService {

    /**
     * Получает список всех пользователей с возможностью фильтрации и постраничной навигации.
     *
     * @param userFilter параметры фильтрации пользователей
     * @param pageable   параметры пагинации
     * @return страница с пользователями в формате {@link UserResponse}
     */
    Page<UserResponse> getAllUsers(UserFilter userFilter, Pageable pageable);

    /**
     * Получает пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя
     * @return данные пользователя в формате {@link UserResponse}
     */
    UserResponse getUserById(UUID id);

    /**
     * Обновляет данные пользователя.
     *
     * @param id           уникальный идентификатор пользователя
     * @param userRequest  новые данные пользователя
     * @return обновлённый пользователь в формате {@link UserResponse}
     */
    UserResponse updateUser(UUID id, UserRequest userRequest);

    /**
     * Удаляет пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя
     */
    void deleteUser(UUID id);
}
