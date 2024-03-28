package com.festago.mock.application;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class RandomFestivalDateGenerator implements FestivalDateGenerator{

    private static final int COUNT_FIRST_DAY_AS_DURATION_ONE = 1;
    private static final int RANDOM_OFFSET = 1;
    private static final int MAX_END_DATE_FROM_START_DATE = 2;
    private final Random random = new Random();
    
    @Override
    public LocalDate makeRandomStartDate(int festivalDuration, LocalDate now) {
        return now.plusDays(random.nextInt(festivalDuration));
    }

    @Override
    public LocalDate makeRandomEndDate(int festivalDuration, LocalDate now, LocalDate startDate) {
        long timeUntilFestivalStart = startDate.until(now, ChronoUnit.DAYS);
        long maxAvailableEndDateFromStartDate = festivalDuration - (timeUntilFestivalStart + COUNT_FIRST_DAY_AS_DURATION_ONE);
        int randomEndDate = random.nextInt((int) (maxAvailableEndDateFromStartDate + RANDOM_OFFSET));
        return startDate.plusDays(Math.min(randomEndDate, MAX_END_DATE_FROM_START_DATE));
    }
}
