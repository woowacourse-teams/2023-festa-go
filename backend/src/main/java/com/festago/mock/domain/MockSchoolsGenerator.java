package com.festago.mock.domain;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class MockSchoolsGenerator {

    private static final int SCHOOL_PER_REGION = 3;

    public List<School> generate() {
        return Arrays.stream(SchoolRegion.values())
            .filter(it -> it != SchoolRegion.ANY)
            .flatMap(this::crateSchools)
            .toList();
    }

    private Stream<School> crateSchools(SchoolRegion schoolRegion) {
        return IntStream.rangeClosed(1, SCHOOL_PER_REGION)
            .mapToObj(i -> {
                String schoolName = String.format("%s%d대학교", schoolRegion.name(), i);
                String schoolEmail = String.format("%s%d.com", schoolRegion.name(), i);
                return crateSchool(schoolRegion, schoolName, schoolEmail);
            });
    }

    private School crateSchool(SchoolRegion schoolRegion, String schoolName, String schoolEmail) {
        return new School(
            schoolEmail,
            schoolName,
            "",
            "",
            schoolRegion
        );
    }
}
