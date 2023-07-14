package com.festago.config;

import com.festago.domain.EntryCodeProvider;
import com.festago.infrastructure.JwtEntryCodeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntryProviderConfig {

    private final String secretKey;

    public EntryProviderConfig(@Value("${festago.qr-secret-key}")String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public EntryCodeProvider entryCodeProvider() {
        return new JwtEntryCodeProvider(secretKey);
    }
}
