package com.festago.auth;

import com.festago.auth.annotation.Authorization;
import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.infrastructure.web.HttpRequestTokenExtractor;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import com.festago.common.exception.UnexpectedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AnnotationAuthorizationInterceptor implements HandlerInterceptor {

    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;

    public AnnotationAuthorizationInterceptor(
        HttpRequestTokenExtractor httpRequestTokenExtractor,
        AuthenticationTokenExtractor authenticationTokenExtractor,
        AuthenticateContext authenticateContext) {
        Assert.notNull(httpRequestTokenExtractor, "The httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "The authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
        if (authorization == null) {
            throw new UnexpectedException("HandlerMethod에 Authorization 어노테이션이 없습니다.");
        }
        String token = httpRequestTokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        Authentication authentication = authenticationTokenExtractor.extract(token);
        if (authentication.getRole() != authorization.role()) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        authenticateContext.setAuthentication(authentication);
        return true;
    }
}
