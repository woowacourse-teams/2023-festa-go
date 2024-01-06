package com.festago.support;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockEventPublisherConfig {

    @Bean
    @Primary
    public ApplicationEventPublisher publisher() {
        return Mockito.mock(ApplicationEventPublisher.class);
    }
}
