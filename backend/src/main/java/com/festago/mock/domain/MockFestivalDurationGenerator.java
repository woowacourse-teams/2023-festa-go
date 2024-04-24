package com.festago.mock.domain;

import com.festago.festival.domain.FestivalDuration;
import java.time.LocalDate;

public interface MockFestivalDurationGenerator {

    FestivalDuration generateFestivalDuration(LocalDate standardDate);
}
