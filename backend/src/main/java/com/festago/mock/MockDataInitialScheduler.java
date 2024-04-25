package com.festago.mock;

import com.festago.mock.application.MockDataService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile({"dev"})
@Component
@RequiredArgsConstructor
public class MockDataInitialScheduler {

    private static final long DAYS_OF_WEEK = 7L;
    private final MockDataService mockDataService;

    @Scheduled(initialDelay = DAYS_OF_WEEK, fixedDelay = DAYS_OF_WEEK, timeUnit = TimeUnit.DAYS)
    public void createMockFestivals() {
        mockDataService.makeMockFestivals();
    }
}
