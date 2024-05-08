package com.festago.common.util;

import org.springframework.util.StringUtils;

public class ImageUrlHelper {

    private ImageUrlHelper() {

    }

    public static String getBlankStringIfBlank(String input) {
        if (StringUtils.hasText(input)) {
            return input;
        }
        return "";
    }
}
