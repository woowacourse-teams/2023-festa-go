package com.festago.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorLoggerConfig {

    @Bean
    public Logger errorLogger() {
        return LoggerFactory.getLogger("ErrorLogger");
    }
}
