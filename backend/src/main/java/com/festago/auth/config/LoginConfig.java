package com.festago.auth.config;

import com.festago.auth.AdminAuthenticationArgumentResolver;
import com.festago.auth.AnnotationAuthorizationInterceptor;
import com.festago.auth.FixedAuthorizationInterceptor;
import com.festago.auth.MemberAuthenticationArgumentResolver;
import com.festago.auth.RoleArgumentResolver;
import com.festago.auth.annotation.Authorization;
import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.AuthenticationTokenExtractor;
import com.festago.auth.domain.Role;
import com.festago.auth.infrastructure.web.CompositeHttpRequestTokenExtractor;
import com.festago.auth.infrastructure.web.CookieHttpRequestTokenExtractor;
import com.festago.auth.infrastructure.web.HeaderHttpRequestTokenExtractor;
import com.festago.common.interceptor.AnnotationDelegateInterceptor;
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

    private final AuthenticationTokenExtractor memberAuthenticationTokenExtractor;
    private final AuthenticationTokenExtractor adminAuthenticationTokenExtractor;
    private final AuthenticationTokenExtractor compositeAuthenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RoleArgumentResolver(Role.MEMBER, authenticateContext));
        resolvers.add(new RoleArgumentResolver(Role.ADMIN, authenticateContext));
        resolvers.add(new MemberAuthenticationArgumentResolver(authenticateContext));
        resolvers.add(new AdminAuthenticationArgumentResolver(authenticateContext));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(adminFixedAuthorizationInterceptor())
                .build())
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/api/v1/auth/login", "/admin/api/v1/auth/initialize");
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(memberFixedAuthorizationInterceptor())
                .build())
            .addPathPatterns("/member-tickets/**", "/members/**", "/auth/**", "/students/**", "/member-fcm/**")
            .excludePathPatterns("/auth/oauth2");
        registry.addInterceptor(AnnotationDelegateInterceptor.builder()
                .annotation(Authorization.class)
                .interceptor(annotationAuthorizationInterceptor())
                .build())
            .addPathPatterns("/api/**");
    }

    @Bean
    public FixedAuthorizationInterceptor adminFixedAuthorizationInterceptor() {
        return new FixedAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            adminAuthenticationTokenExtractor,
            authenticateContext,
            Role.ADMIN
        );
    }

    @Bean
    public CompositeHttpRequestTokenExtractor compositeHttpRequestTokenExtractor() {
        return new CompositeHttpRequestTokenExtractor(
            List.of(
                new HeaderHttpRequestTokenExtractor(),
                new CookieHttpRequestTokenExtractor()
            )
        );
    }

    @Bean
    public FixedAuthorizationInterceptor memberFixedAuthorizationInterceptor() {
        return new FixedAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            memberAuthenticationTokenExtractor,
            authenticateContext,
            Role.MEMBER
        );
    }

    @Bean
    public AnnotationAuthorizationInterceptor annotationAuthorizationInterceptor() {
        return new AnnotationAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            compositeAuthenticationTokenExtractor,
            authenticateContext
        );
    }
}
