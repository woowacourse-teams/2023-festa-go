package com.festago.mock;

import com.festago.mock.application.MockDataService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class MockScheduler {

    private static final long SCHEDULER_CYCLE = 7;
    private final MockDataService mockDataService;

    @Scheduled(fixedDelay = SCHEDULER_CYCLE, timeUnit = TimeUnit.DAYS)
    public void run() {
        mockDataService.makeMockFestivals((int) SCHEDULER_CYCLE);
    }
}
