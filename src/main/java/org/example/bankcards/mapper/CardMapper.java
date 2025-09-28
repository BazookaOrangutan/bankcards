package org.example.bankcards.mapper;

import org.example.bankcards.dto.response.CardResponse;
import org.example.bankcards.entity.Card;
import org.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserId")
    CardResponse toResponse(Card card);

    @Named("mapUserId")
    default UUID mapUserId(User user) {
        return user.getId();
    }

}
