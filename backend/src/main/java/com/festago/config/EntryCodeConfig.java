package com.festago.config;

import com.festago.zentry.application.EntryCodeExtractor;
import com.festago.zentry.application.EntryCodeProvider;
import com.festago.zentry.infrastructure.JwtEntryCodeExtractor;
import com.festago.zentry.infrastructure.JwtEntryCodeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntryCodeConfig {

    private final String secretKey;

    public EntryCodeConfig(@Value("${festago.qr-secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public EntryCodeProvider entryCodeProvider() {
        return new JwtEntryCodeProvider(secretKey);
    }

    @Bean
    public EntryCodeExtractor entryCodeExtractor() {
        return new JwtEntryCodeExtractor(secretKey);
    }
}
