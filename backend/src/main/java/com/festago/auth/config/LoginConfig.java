package com.festago.auth.config;

import com.festago.auth.domain.AdminAuthInterceptor;
import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.presentation.LoginMemberResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;

    public LoginConfig(AuthExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberResolver(authExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthInterceptor(authExtractor))
            .addPathPatterns("/admin/**", "/js/admin/**")
            .excludePathPatterns("/admin/login", "/admin/initialize", "/js/**", "/css/**");
    }
}
