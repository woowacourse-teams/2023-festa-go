package com.festago.auth.config;

import com.festago.auth.AuthInterceptor;
import com.festago.auth.AuthenticateContext;
import com.festago.auth.RoleArgumentResolver;
import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.Role;
import com.festago.auth.infrastructure.CookieTokenExtractor;
import com.festago.auth.infrastructure.HeaderTokenExtractor;
import com.festago.common.interceptor.HttpMethodDelegateInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(adminAuthInterceptor())
                .build())
            .addPathPatterns("/admin/**", "/js/admin/**")
            .excludePathPatterns("/admin/login", "/admin/api/login", "/admin/api/initialize");
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(memberAuthInterceptor())
                .build())
            .addPathPatterns("/member-tickets/**", "/members/**", "/auth/**", "/students/**", "/member-fcm/**")
            .excludePathPatterns("/auth/oauth2");
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
}
