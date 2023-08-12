package com.festago.auth.presentation;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Login;
import com.festago.auth.dto.LoginMember;
import com.festago.exception.ErrorCode;
import com.festago.exception.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final AuthExtractor authExtractor;

    public LoginMemberResolver(AuthExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class) && parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = extractToken(header);
        AuthPayload authPayload = authExtractor.extract(token);
        return new LoginMember(authPayload.getMemberId());
    }

    private String extractToken(String header) {
        validateHeader(header);
        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private void validateHeader(String header) {
        if (header == null) {
            throw new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN);
        }
        if (!header.toLowerCase().startsWith(BEARER_TOKEN_PREFIX.toLowerCase())) {
            throw new UnauthorizedException(ErrorCode.NOT_BEARER_TOKEN_TYPE);
        }
    }
}
