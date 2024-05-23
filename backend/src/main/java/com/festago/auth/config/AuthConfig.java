package com.festago.auth.config;

import com.festago.auth.application.OAuth2Client;
import com.festago.auth.application.OAuth2Clients;
import com.festago.auth.domain.OpenIdClient;
import com.festago.auth.domain.OpenIdClients;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public OAuth2Clients oAuth2Clients(List<OAuth2Client> oAuth2Clients) {
        return OAuth2Clients.builder()
            .addAll(oAuth2Clients)
            .build();
    }

    @Bean
    public OpenIdClients openIdClients(List<OpenIdClient> openIdClients) {
        return OpenIdClients.builder()
            .addAll(openIdClients)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
