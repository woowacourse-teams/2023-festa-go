package com.festago.mock.domain;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockStagesGenerator {

    private static final long INCLUSIVE_VALUE = 1L;
    private static final int STAGE_START_HOUR = 19;

    public List<Stage> generate(Festival festival) {
        LocalDate startDate = festival.getStartDate();
        LocalDate endDate = festival.getEndDate();
        return startDate.datesUntil(endDate.plusDays(INCLUSIVE_VALUE))
            .map(stageDate -> createStage(festival, stageDate))
            .toList();
    }

    private Stage createStage(Festival festival, LocalDate stageDate) {
        LocalDateTime startTime = stageDate.atTime(STAGE_START_HOUR, 0);
        return new Stage(startTime, startTime.minusWeeks(1), festival);
    }
}
