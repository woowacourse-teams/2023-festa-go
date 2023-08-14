package com.festago.auth.domain;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";

    private final AuthExtractor authExtractor;

    public AdminAuthInterceptor(AuthExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return redirectLoginPage(response);
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(TOKEN, cookie.getName())) {
                AuthPayload payload = authExtractor.extract(cookie.getValue());
                if (payload.getRole() == Role.ADMIN) {
                    return true;
                }
                return redirectLoginPage(response);
            }
        }
        return redirectLoginPage(response);
    }

    private boolean redirectLoginPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/admin/login");
        return false;
    }
}
