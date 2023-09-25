package com.festago.common.util;

import org.springframework.util.StringUtils;

public abstract class Validator {

    public static void maxLength(CharSequence input, int maxLength, String message) {
        if (!StringUtils.hasText(input) || input.length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }
}
