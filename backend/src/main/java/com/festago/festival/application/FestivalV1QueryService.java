package com.festago.festival.application;

import com.festago.festival.dto.v1.FestivalListRequest;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalV1QueryService {

    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public void findFestivals(FestivalListRequest request) {
        if (request.getLocation() == SchoolRegion.기타) {
            return;
        }
        findTargetRegionFestival(schoolRepository.findAllByRegion(request.getLocation()));
    }

    private void findTargetRegionFestival(List<School> targetRegionSchools) {
        return;
    }
}
