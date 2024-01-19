package com.festago.common;

import com.festago.common.converter.FestivalFilterConverter;
import com.festago.common.converter.FestivalPageLimitConverter;
import com.festago.common.converter.SchoolRegionConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FestivalFilterConverter());
        registry.addConverter(new FestivalPageLimitConverter());
        registry.addConverter(new SchoolRegionConverter());
    }
}
