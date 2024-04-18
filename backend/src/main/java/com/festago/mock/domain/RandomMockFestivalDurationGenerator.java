package com.festago.mock.domain;

import com.festago.festival.domain.FestivalDuration;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomMockFestivalDurationGenerator implements MockFestivalDurationGenerator {

    private static final long DAYS_OF_WEEK = 7L;
    private static final long INCLUSIVE_OFFSET = 1L;
    private final Random random = ThreadLocalRandom.current();

    @Override
    public FestivalDuration generateFestivalDuration(LocalDate standardDate) {
        long daysToAddStartDate = random.nextLong(DAYS_OF_WEEK + INCLUSIVE_OFFSET); // 0 ~ 7
        long daysToAddEndDate = random.nextLong(
            DAYS_OF_WEEK + INCLUSIVE_OFFSET - daysToAddStartDate); // 0 ~ 7. if daysToAddStartDate == 7 ? 0
        LocalDate startDate = standardDate.plusDays(daysToAddStartDate);
        LocalDate endDate = startDate.plusDays(daysToAddEndDate);
        return new FestivalDuration(startDate, endDate);
    }
}
