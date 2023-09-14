package com.festago.presentation.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ErrorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            if (Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
                return;
            }
            request.getRequestDispatcher("/error/404").forward(request, response);
        }
    }
}
