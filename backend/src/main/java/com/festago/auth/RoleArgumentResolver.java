package com.festago.auth;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.Role;
import com.festago.common.exception.UnexpectedException;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @deprecated 기존 Long으로 식별자를 받는 Controller가 많기에, 해당 클래스 삭제하지 않고 유지
 */
@Deprecated
public class RoleArgumentResolver implements HandlerMethodArgumentResolver {

    private final Role role;
    private final AuthenticateContext authenticateContext;

    public RoleArgumentResolver(Role role, AuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.role = role;
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(
            role.getAnnotation());
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        if (authenticateContext.getRole() != this.role) {
            throw new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.");
        }
        return authenticateContext.getId();
    }
}
