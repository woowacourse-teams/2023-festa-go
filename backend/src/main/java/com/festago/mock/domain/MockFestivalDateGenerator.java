package com.festago.mock.domain;

import java.time.LocalDate;

public interface MockFestivalDateGenerator {

    LocalDate generateStartDate(int festivalDuration, LocalDate now);

    LocalDate generateEndDate(int festivalDuration, LocalDate now, LocalDate startDate);
}
