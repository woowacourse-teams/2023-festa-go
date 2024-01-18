package com.festago.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final List<String> allowUrls;

    public WebConfig(@Value("${festago.cors-allow-urls}") List<String> allowUrls) {
        this.allowUrls = allowUrls;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(getAllowedOriginPatterns())
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }

    private String[] getAllowedOriginPatterns() {
        return allowUrls.stream()
            .map(it -> it + "**")
            .toArray(String[]::new);
    }
}
