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
import java.util.EnumMap;
import java.util.List;
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
        EnumMap<FestivalFilter, List<FestivalSearchV1Response>> festivalByStatus = divideByStatus(festivals);
        List<FestivalSearchV1Response> result = new ArrayList<>();
        result.addAll(sortByStatus(PROGRESS, festivalByStatus.get(PROGRESS)));
        result.addAll(sortByStatus(PLANNED, festivalByStatus.get(PLANNED)));
        result.addAll(sortByStatus(END, festivalByStatus.get(END)));
        return result;
    }

    private EnumMap<FestivalFilter, List<FestivalSearchV1Response>> divideByStatus(
        List<FestivalSearchV1Response> festivals) {
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

    private List<FestivalSearchV1Response> sortByStatus(
        FestivalFilter status,
        List<FestivalSearchV1Response> festivals) {
        if (festivals == null) {
            return Collections.emptyList();
        }
        if (status == END) {
            sortEndFestival(festivals);
        }
        if (status == PROGRESS) {
            sortProgressFestival(festivals);
        }
        if (status == PLANNED) {
            sortPlannedFestival(festivals);
        }
        return festivals;
    }

    private void sortEndFestival(List<FestivalSearchV1Response> festivals) {
        festivals.sort((festival1, festival2) -> {
            if (festival1.endDate().isAfter(festival2.endDate())) {
                return 1;
            }
            if (festival1.endDate().isEqual(festival2.endDate())) {
                return 0;
            }
            return -1;
        });
    }

    private void sortProgressFestival(List<FestivalSearchV1Response> festivals) {
        festivals.sort((festival1, festival2) -> {
            if (festival1.startDate().isBefore(festival2.endDate())) {
                return 1;
            }
            if (festival1.startDate().isEqual(festival2.startDate())) {
                return 0;
            }
            return -1;
        });
    }

    private void sortPlannedFestival(List<FestivalSearchV1Response> festivals) {
        festivals.sort((festival1, festival2) -> {
            if (festival1.startDate().isBefore(festival2.endDate())) {
                return 1;
            }
            if (festival1.startDate().isEqual(festival2.startDate())) {
                return 0;
            }
            return -1;
        });
    }
}
