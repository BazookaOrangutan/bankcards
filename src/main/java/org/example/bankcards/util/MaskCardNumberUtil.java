package org.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class MaskCardNumberUtil {

    public static String maskCardNumber(String cardNumber) {

        String last4 = cardNumber.substring(cardNumber.length() - 4);

        return "**** **** **** " + last4;
    }
}
