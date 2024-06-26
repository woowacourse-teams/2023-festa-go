package com.festago.mock;

import com.festago.mock.application.MockDataService;
import com.festago.mock.repository.ForMockSchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev"})
@Component
@RequiredArgsConstructor
public class MockDataStartupInitializer {

    private final ForMockSchoolRepository forMockSchoolRepository;
    private final MockDataService mockDataService;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        if (forMockSchoolRepository.count() == 0) {
            mockDataService.makeMockArtists();
            mockDataService.makeMockSchools();
            mockDataService.makeMockFestivals();
        }
    }
}
