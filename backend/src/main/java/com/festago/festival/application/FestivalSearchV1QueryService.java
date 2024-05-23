package com.festago.festival.application;

import static com.festago.festival.repository.FestivalFilter.END;
import static com.festago.festival.repository.FestivalFilter.PLANNED;
import static com.festago.festival.repository.FestivalFilter.PROGRESS;

import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.repository.ArtistFestivalSearchV1QueryDslRepository;
import com.festago.festival.repository.FestivalFilter;
import com.festago.festival.repository.SchoolFestivalSearchV1QueryDslRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalSearchV1QueryService {

    private final ArtistFestivalSearchV1QueryDslRepository artistFestivalSearchV1QueryDslRepository;
    private final SchoolFestivalSearchV1QueryDslRepository schoolFestivalSearchV1QueryDslRepository;

    public List<FestivalSearchV1Response> search(String keyword) {
        return sortFestival(findFestivals(keyword));
    }

    private List<FestivalSearchV1Response> findFestivals(String keyword) {
        if (artistFestivalSearchV1QueryDslRepository.existsByName(keyword)) {
            return artistFestivalSearchV1QueryDslRepository.executeSearch(keyword);
        }
        return schoolFestivalSearchV1QueryDslRepository.executeSearch(keyword);
    }

    private List<FestivalSearchV1Response> sortFestival(List<FestivalSearchV1Response> festivals) {
        Map<FestivalFilter, List<FestivalSearchV1Response>> festivalByStatus = divideByStatus(festivals);
        List<FestivalSearchV1Response> result = new ArrayList<>();
        for (FestivalFilter festivalFilter : determineFestivalOrder()) {
            List<FestivalSearchV1Response> festivalsByFilter = festivalByStatus.getOrDefault(festivalFilter,
                Collections.emptyList());
            sortByStatus(festivalFilter, festivalsByFilter);
            result.addAll(festivalsByFilter);
        }
        return result;
    }

    private Map<FestivalFilter, List<FestivalSearchV1Response>> divideByStatus(
        List<FestivalSearchV1Response> festivals
    ) {
        return festivals.stream()
            .collect(Collectors.groupingBy(
                this::determineStatus,
                () -> new EnumMap<>(FestivalFilter.class),
                Collectors.toList()
            ));
    }

    private FestivalFilter determineStatus(FestivalSearchV1Response festival) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(festival.endDate())) {
            return END;
        }
        if (now.isBefore(festival.startDate())) {
            return PLANNED;
        }
        return PROGRESS;
    }

    private List<FestivalFilter> determineFestivalOrder() {
        return List.of(PROGRESS, PLANNED, END);
    }

    private void sortByStatus(
        FestivalFilter status,
        List<FestivalSearchV1Response> festivals) {

        switch (status) {
            case END -> festivals.sort(Comparator.comparing(FestivalSearchV1Response::endDate).reversed());
            case PROGRESS, PLANNED -> festivals.sort(Comparator.comparing(FestivalSearchV1Response::startDate));
        }
    }
}
