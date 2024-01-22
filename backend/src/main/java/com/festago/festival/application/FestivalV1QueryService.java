package com.festago.festival.application;

import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfo;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.dto.v1.FestivalV1ListResponse;
import com.festago.festival.dto.v1.FestivalV1lListRequest;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalPage;
import com.festago.festival.repository.FestivalPageable;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalV1QueryService {

    private final SchoolRepository schoolRepository;
    private final FestivalInfoRepository festivalInfoRepository;
    private final FestivalRepository festivalRepository;
    private final FestivalInfoSerializer infoSerializer;
    private final Clock clock;

    public FestivalV1ListResponse findFestivals(FestivalV1lListRequest request) {
        LocalDate now = LocalDate.now(clock);
        if (request.getLocation() == SchoolRegion.기타) {
            return makeResponse(festivalRepository.findBy(request.getFilter(), makePageable(request), now));
        }
        return makeResponse(
            findTargetRegionFestival(request, schoolRepository.findAllByRegion(request.getLocation()), now));
    }

    private FestivalV1ListResponse makeResponse(FestivalPage festivalPage) {
        List<Long> festivalIds = festivalPage.getFestivals().stream()
            .map(Festival::getId)
            .toList();
        List<FestivalInfo> festivalInfos = festivalInfoRepository.findAllByFestivalIdIn(festivalIds);
        return FestivalV1ListResponse.of(festivalPage.isLastPage(),
            festivalPage.getFestivals(),
            festivalInfos,
            infoSerializer);
    }

    private FestivalPageable makePageable(FestivalV1lListRequest request) {
        if (request.getLastStartDate().isPresent() && request.getLastFestivalId().isPresent()) {
            return new FestivalPageable(request.getLastStartDate().get(), request.getLastFestivalId().get(),
                request.getLimit());
        }
        return new FestivalPageable(null, null, request.getLimit());
    }

    private FestivalPage findTargetRegionFestival(FestivalV1lListRequest request, List<School> targetRegionSchools,
                                                  LocalDate now) {
        return festivalRepository.findBy(request.getFilter(), targetRegionSchools, makePageable(request), now);
    }
}
