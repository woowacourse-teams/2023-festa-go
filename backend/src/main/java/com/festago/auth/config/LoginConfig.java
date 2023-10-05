package com.festago.auth.config;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.Role;
import com.festago.auth.infrastructure.CookieTokenExtractor;
import com.festago.auth.infrastructure.HeaderTokenExtractor;
import com.festago.presentation.auth.AuthInterceptor;
import com.festago.presentation.auth.AuthenticateContext;
import com.festago.presentation.auth.RoleArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;
    private final AuthenticateContext authenticateContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RoleArgumentResolver(Role.MEMBER, authenticateContext));
        resolvers.add(new RoleArgumentResolver(Role.ADMIN, authenticateContext));
        resolvers.add(new RoleArgumentResolver(Role.STAFF, authenticateContext));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor())
            .addPathPatterns("/admin/**", "/js/admin/**")
            .excludePathPatterns("/admin/login", "/admin/initialize");
        registry.addInterceptor(memberAuthInterceptor())
            .addPathPatterns("/member-tickets/**", "/members/**", "/auth/**", "/students/**")
            .excludePathPatterns("/auth/oauth2");
        registry.addInterceptor(staffAuthInterceptor())
            .addPathPatterns("/staff/**")
            .excludePathPatterns("/staff/login");
    }

    @Bean
    public AuthInterceptor adminAuthInterceptor() {
        return AuthInterceptor.builder()
            .authExtractor(authExtractor)
            .tokenExtractor(new CookieTokenExtractor())
            .authenticateContext(authenticateContext)
            .role(Role.ADMIN)
            .build();
    }

    @Bean
    public AuthInterceptor memberAuthInterceptor() {
        return AuthInterceptor.builder()
            .authExtractor(authExtractor)
            .tokenExtractor(new HeaderTokenExtractor())
            .authenticateContext(authenticateContext)
            .role(Role.MEMBER)
            .build();
    }

    @Bean
    public AuthInterceptor staffAuthInterceptor() {
        return AuthInterceptor.builder()
            .authExtractor(authExtractor)
            .tokenExtractor(new HeaderTokenExtractor())
            .authenticateContext(authenticateContext)
            .role(Role.STAFF)
            .build();
    }
}
