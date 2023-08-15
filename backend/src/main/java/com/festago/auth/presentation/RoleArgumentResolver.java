package com.festago.auth.presentation;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.Role;
import com.festago.exception.ErrorCode;
import com.festago.exception.ForbiddenException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RoleArgumentResolver implements HandlerMethodArgumentResolver {

    private final Role role;
    private final AuthenticateContext authenticationContext;

    public RoleArgumentResolver(Role role, AuthenticateContext authenticationContext) {
        this.role = role;
        this.authenticationContext = authenticationContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(
            role.getAnnotation());
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (authenticationContext.getRole() != this.role) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        return authenticationContext.getId();
    }
}
