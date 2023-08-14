package com.festago.auth.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.ForbiddenException;
import com.festago.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class MemberAuthInterceptor implements HandlerInterceptor {

    private final AuthExtractor authExtractor;
    private final TokenExtractor tokenExtractor;
    private final AuthenticateContext context;

    public MemberAuthInterceptor(AuthExtractor authExtractor, TokenExtractor tokenExtractor,
                                 AuthenticateContext context) {
        this.authExtractor = authExtractor;
        this.tokenExtractor = tokenExtractor;
        this.context = context;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String token = tokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        AuthPayload payload = authExtractor.extract(token);
        if (payload.getRole() != Role.MEMBER) {
            throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
        context.setAuthenticate(payload.getMemberId(), payload.getRole());
        return true;
    }
}
