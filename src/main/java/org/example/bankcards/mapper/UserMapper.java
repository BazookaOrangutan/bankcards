package org.example.bankcards.mapper;

import org.example.bankcards.dto.response.UserResponse;
import org.example.bankcards.entity.Card;
import org.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "cards", target = "cardIds", qualifiedByName = "mapCardIds")
    @Mapping(target = "username", expression = "java(user.getUserDetails().getUsername())")
    @Mapping(target = "email", expression = "java(user.getUserDetails().getEmail())")
    @Mapping(target = "role", expression = "java(user.getUserDetails().getRole())")
    UserResponse toResponse(User user);

    @Named("mapCardIds")
    default List<UUID> mapCardIds(List<Card> cards) {
        return cards != null ? cards.stream().map(Card::getId).toList() : Collections.emptyList();
    }

}
