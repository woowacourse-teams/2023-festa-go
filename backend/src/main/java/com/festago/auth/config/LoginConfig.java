package com.festago.auth.config;

import com.festago.auth.domain.AdminAuthInterceptor;
import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.MemberAuthInterceptor;
import com.festago.auth.infrastructure.CookieTokenExtractor;
import com.festago.auth.infrastructure.HeaderTokenExtractor;
import com.festago.auth.presentation.AdminArgumentResolver;
import com.festago.auth.presentation.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;
    private final AuthenticateContext authenticateContext;

    public LoginConfig(AuthExtractor authExtractor, AuthenticateContext context) {
        this.authExtractor = authExtractor;
        this.authenticateContext = context;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AdminArgumentResolver(authenticateContext));
        resolvers.add(new MemberArgumentResolver(authenticateContext));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor())
            .addPathPatterns("/admin/**", "/js/admin/**")
            .excludePathPatterns("/admin/login", "/admin/initialize", "/js/**", "/css/**");
        registry.addInterceptor(memberAuthInterceptor())
            .addPathPatterns("/member-tickets/**", "/members/**");
    }

    @Bean
    public AdminAuthInterceptor adminAuthInterceptor() {
        return new AdminAuthInterceptor(authExtractor, new CookieTokenExtractor(), authenticateContext);
    }

    @Bean
    public MemberAuthInterceptor memberAuthInterceptor() {
        return new MemberAuthInterceptor(authExtractor, new HeaderTokenExtractor(), authenticateContext);
    }
}
