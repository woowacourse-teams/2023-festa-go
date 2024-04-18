package com.festago.mock.domain;

import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalDuration;
import com.festago.school.domain.School;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockFestivalsGenerator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final Clock clock;
    private final MockFestivalDurationGenerator festivalDurationGenerator;

    public List<Festival> generate(List<School> schools) {
        LocalDate now = LocalDate.now(clock);
        return schools.stream()
            .map(school -> {
                FestivalDuration festivalDuration = festivalDurationGenerator.generateFestivalDuration(now);
                LocalDate startDate = festivalDuration.getStartDate();
                LocalDate endDate = festivalDuration.getEndDate();
                return new Festival(
                    school.getName() + " " + startDate.format(DATE_TIME_FORMATTER) + " 축제",
                    startDate,
                    endDate,
                    school
                );
            })
            .toList();
    }
}
