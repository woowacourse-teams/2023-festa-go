package com.festago.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AuthExtractor authExtractor;
    private final TokenExtractor tokenExtractor;
    private final AuthenticateContext context;

    public AdminAuthInterceptor(AuthExtractor authExtractor, TokenExtractor tokenExtractor,
                                AuthenticateContext context) {
        this.authExtractor = authExtractor;
        this.tokenExtractor = tokenExtractor;
        this.context = context;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        Optional<String> token = tokenExtractor.extract(request);
        if (token.isEmpty()) {
            return redirectLoginPage(response);
        }
        AuthPayload payload = authExtractor.extract(token.get());
        if (payload.getRole() != Role.ADMIN) {
            return redirectLoginPage(response);
        }
        context.setAuthenticate(payload.getMemberId(), payload.getRole());
        return true;
    }

    private boolean redirectLoginPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/admin/login");
        return false;
    }
}
