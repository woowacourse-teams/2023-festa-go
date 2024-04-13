package com.festago.mock;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev"})
@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private static final int AVAILABLE_FESTIVAL_DURATION = 7;
    private final MockDataService mockDataService;

    @Override
    public void run(String... args) {
        mockDataService.initialize();
        mockDataService.makeMockFestivals(AVAILABLE_FESTIVAL_DURATION);
    }
}
