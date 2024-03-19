package com.festago.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
        if (uriPatternMatcher.match(request.getMethod().toUpperCase(), request.getRequestURI())) {
            ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
            chain.doFilter(wrappingRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}

