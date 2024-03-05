package com.festago.mock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MockScheduler {

    @Scheduled(fixedDelay = 1000)
    public void run() {
        System.out.println("called");
    }
}
