package com.festago.auth;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.application.TokenExtractor;
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

    private final AuthExtractor authExtractor;
    private final TokenExtractor tokenExtractor;
    private final AuthenticateContext authenticateContext;
    private final Role role;

    private AuthInterceptor(AuthExtractor authExtractor, TokenExtractor tokenExtractor,
                            AuthenticateContext authenticateContext, Role role) {
        Assert.notNull(authExtractor, "The authExtractor must not be null");
        Assert.notNull(tokenExtractor, "The tokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.authExtractor = authExtractor;
        this.tokenExtractor = tokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    public static AuthInterceptorBuilder builder() {
        return new AuthInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String token = tokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        AuthPayload payload = authExtractor.extract(token);
        if (payload.getRole() != this.role) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        authenticateContext.setAuthenticate(payload.getMemberId(), payload.getRole());
        return true;
    }

    public static class AuthInterceptorBuilder {

        private AuthExtractor authExtractor;
        private TokenExtractor tokenExtractor;
        private AuthenticateContext authenticateContext;
        private Role role;

        public AuthInterceptorBuilder authExtractor(AuthExtractor authExtractor) {
            this.authExtractor = authExtractor;
            return this;
        }

        public AuthInterceptorBuilder authenticateContext(AuthenticateContext authenticateContext) {
            this.authenticateContext = authenticateContext;
            return this;
        }

        public AuthInterceptorBuilder tokenExtractor(TokenExtractor tokenExtractor) {
            this.tokenExtractor = tokenExtractor;
            return this;
        }

        public AuthInterceptorBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public AuthInterceptor build() {
            return new AuthInterceptor(authExtractor, tokenExtractor, authenticateContext, role);
        }
    }
}
