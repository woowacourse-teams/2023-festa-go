package com.festago.config;

import com.festago.common.filter.wrapping.UriPatternMatcher;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String allowOrigins;

    public WebConfig(@Value("${festago.cors-allow-origins}") String allowOrigins) {
        this.allowOrigins = allowOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(allowOrigins)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.KOREA);
    }

    // WebMvcConfig 테스트에서 의존성 문제 때문에 Bean으로 여기서 등록
    @Bean
    public UriPatternMatcher urlMatcher() {
        return new UriPatternMatcher();
    }
}
