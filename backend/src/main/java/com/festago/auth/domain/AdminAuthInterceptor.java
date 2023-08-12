package com.festago.auth.domain;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AuthExtractor authExtractor;

    public AdminAuthInterceptor(AuthExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals("token", cookie.getName())) {
                    AuthPayload payload = authExtractor.extract(cookie.getValue());
                    if (payload.getRole() == Role.ADMIN) {
                        return true;
                    }
                }
            }
        }
        response.sendRedirect("/admin/login");
        return false;
    }
}
