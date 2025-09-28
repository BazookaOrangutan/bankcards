package org.example.bankcards.util.converter;

import org.example.bankcards.entity.CardStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CardStatusConverter implements Converter<String, CardStatus> {

    @Override
    public CardStatus convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return CardStatus.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card status: " + source +
                    ". Allowed values: " + Arrays.toString(CardStatus.values()));
        }
    }
}