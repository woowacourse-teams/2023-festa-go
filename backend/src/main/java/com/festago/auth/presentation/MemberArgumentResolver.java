package com.festago.auth.presentation;

import com.festago.auth.annotation.Member;
import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.Role;
import com.festago.exception.ErrorCode;
import com.festago.exception.ForbiddenException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticateContext authenticationContext;

    public MemberArgumentResolver(AuthenticateContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(Member.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Role role = authenticationContext.getRole();
        if (role != Role.MEMBER) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        return authenticationContext.getId();
    }
}
