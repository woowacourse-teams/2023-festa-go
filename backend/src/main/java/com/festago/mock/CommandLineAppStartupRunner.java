package com.festago.mock;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev"})
@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final MockDataService mockDataService;
    private final MockScheduler mockScheduler;

    @Override
    public void run(String... args) {
        if (mockDataService.initialize()) {
            mockScheduler.run();
        }
    }
}
