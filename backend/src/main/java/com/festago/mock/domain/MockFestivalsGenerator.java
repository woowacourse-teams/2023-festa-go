package com.festago.mock.domain;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockFestivalsGenerator {

    private static final AtomicLong festivalSequence = new AtomicLong();

    private final Clock clock;
    private final MockFestivalDateGenerator festivalDateGenerator;

    public List<Festival> generate(List<School> schools, int duration) {
        LocalDate now = LocalDate.now(clock);
        return schools.stream()
            .map(school -> {
                LocalDate startDate = festivalDateGenerator.generateStartDate(duration, now);
                LocalDate endDate = festivalDateGenerator.generateEndDate(duration, now, startDate);
                return new Festival(
                    school.getName() + " 축제 " + festivalSequence.incrementAndGet(),
                    startDate,
                    endDate,
                    school
                );
            })
            .toList();
    }
}
