package com.festago.mock.application;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final MockDataInitializer mockDataInitializer;

    @Override
    public void run(String... args) {
        mockDataInitializer.initialize();
    }
}
