package com.festago.auth;

import com.festago.auth.application.AuthTokenExtractor;
import com.festago.auth.application.HttpRequestTokenExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private final AuthTokenExtractor authTokenExtractor;
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticateContext authenticateContext;
    private final Role role;

    private AuthInterceptor(AuthTokenExtractor authTokenExtractor, HttpRequestTokenExtractor httpRequestTokenExtractor,
                            AuthenticateContext authenticateContext, Role role) {
        Assert.notNull(authTokenExtractor, "The authExtractor must not be null");
        Assert.notNull(httpRequestTokenExtractor, "The tokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.authTokenExtractor = authTokenExtractor;
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    public static AuthInterceptorBuilder builder() {
        return new AuthInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String token = httpRequestTokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        AuthPayload payload = authTokenExtractor.extract(token);
        if (payload.getRole() != this.role) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        authenticateContext.setAuthenticate(payload.getMemberId(), payload.getRole());
        return true;
    }

    public static class AuthInterceptorBuilder {

        private AuthTokenExtractor authTokenExtractor;
        private HttpRequestTokenExtractor httpRequestTokenExtractor;
        private AuthenticateContext authenticateContext;
        private Role role;

        public AuthInterceptorBuilder authExtractor(AuthTokenExtractor authTokenExtractor) {
            this.authTokenExtractor = authTokenExtractor;
            return this;
        }

        public AuthInterceptorBuilder authenticateContext(AuthenticateContext authenticateContext) {
            this.authenticateContext = authenticateContext;
            return this;
        }

        public AuthInterceptorBuilder tokenExtractor(HttpRequestTokenExtractor httpRequestTokenExtractor) {
            this.httpRequestTokenExtractor = httpRequestTokenExtractor;
            return this;
        }

        public AuthInterceptorBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public AuthInterceptor build() {
            return new AuthInterceptor(authTokenExtractor, httpRequestTokenExtractor, authenticateContext, role);
        }
    }
}
