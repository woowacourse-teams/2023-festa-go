package com.festago.mock.domain;

import com.festago.mock.repository.ForMockSchoolRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(0)
@Component
@Transactional
@RequiredArgsConstructor
public class MockSchoolDataInitializer implements MockDataInitializer {

    private static final int SCHOOL_PER_REGION = 3;
    private final ForMockSchoolRepository schoolRepository;
    private final SchoolCommandService schoolCommandService;

    @Override
    public boolean canInitialize() {
        return schoolRepository.count() == 0;
    }

    @Override
    public void initialize() {
        Arrays.stream(SchoolRegion.values())
            .filter(it -> it != SchoolRegion.ANY)
            .forEach(this::makeRegionSchools);
    }

    /**
     * 각 지역 별로 3개의 학교를 만듭니다. ex) 서울1대학교 서울2대학교 서울3대학교
     */
    private void makeRegionSchools(SchoolRegion schoolRegion) {
        for (int i = 0; i < SCHOOL_PER_REGION; i++) {
            String schoolName = String.format("%s%d대학교", schoolRegion.name(), i + 1);
            String schoolEmail = String.format("%s%d.com", schoolRegion.name(), i + 1);
            crateSchool(schoolRegion, schoolName, schoolEmail);
        }
    }

    private void crateSchool(SchoolRegion schoolRegion, String schoolName, String schoolEmail) {
        schoolCommandService.createSchool(new SchoolCreateCommand(
                schoolName,
                schoolEmail,
                schoolRegion,
                null,
                null
            )
        );
    }
}
