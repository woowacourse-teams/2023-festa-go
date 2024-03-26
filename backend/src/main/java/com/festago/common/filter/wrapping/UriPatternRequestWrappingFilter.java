package com.festago.common.filter.wrapping;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * LogRequestBodyAspect 클래스가 해당 클래스에 의존하므로, 해당 클래스 수정, 삭제 시 LogRequestBodyAspect 클래스도 수정하거나 삭제할 것!
 */
@Profile("!test")
@Component
@RequiredArgsConstructor
public class UriPatternRequestWrappingFilter extends OncePerRequestFilter {

    private final UriPatternMatcher uriPatternMatcher;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        if (uriPatternMatcher.match(RequestMethod.resolve(request.getMethod()), request.getRequestURI())) {
            ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
            chain.doFilter(wrappingRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}

