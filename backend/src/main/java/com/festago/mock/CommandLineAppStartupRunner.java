package com.festago.mock;

import com.festago.mock.application.MockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final MockDataService mockDataService;

    @Override
    public void run(String... args) {
        mockDataService.initialize();
    }
}
