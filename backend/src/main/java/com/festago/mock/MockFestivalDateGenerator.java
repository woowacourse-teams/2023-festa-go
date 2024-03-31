package com.festago.mock;

import java.time.LocalDate;

public interface MockFestivalDateGenerator {

    LocalDate makeRandomStartDate(int festivalDuration, LocalDate now);

    LocalDate makeRandomEndDate(int festivalDuration, LocalDate now, LocalDate startDate);
}
