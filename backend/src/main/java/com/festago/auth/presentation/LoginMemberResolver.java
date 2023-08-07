package com.festago.auth.presentation;

import com.festago.auth.application.AuthService;
import com.festago.auth.dto.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginMemberResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return authService.loginMemberByHeader(header);
    }
}
