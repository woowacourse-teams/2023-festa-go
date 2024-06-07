package com.festago.auth.infrastructure.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

public class CookieHttpRequestTokenExtractor implements HttpRequestTokenExtractor {

    private static final String TOKEN = "token";

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(TOKEN, cookie.getName())) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
