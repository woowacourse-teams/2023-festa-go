package com.festago.auth;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.web.HttpRequestTokenExtractor;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

public class FixedAuthorizationInterceptor implements HandlerInterceptor {

    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;
    private final Role role;

    public FixedAuthorizationInterceptor(
        HttpRequestTokenExtractor httpRequestTokenExtractor,
        AuthenticationTokenExtractor authenticationTokenExtractor,
        AuthenticateContext authenticateContext,
        Role role
    ) {
        Assert.notNull(httpRequestTokenExtractor, "The httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "The authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = httpRequestTokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        Authentication authentication = authenticationTokenExtractor.extract(token);
        if (authentication.getRole() != role) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        authenticateContext.setAuthentication(authentication);
        return true;
    }
}
