package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FestivalRepositoryCustomImpl implements FestivalRepositoryCustom {

    private final OldFestivalRepositoryCustomImpl oldFestivalRepositoryCustom;

    private final V1FestivalRepositoryCustomImpl v1FestivalRepositoryCustom;

    @Override
    public List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime) {
        return oldFestivalRepositoryCustom.findByFilter(filter, currentTime);
    }

    @Override
    public FestivalPage findBy(FestivalFilter filter, FestivalPageable page, LocalDate currentTime) {
        return v1FestivalRepositoryCustom.findBy(filter, page, currentTime);
    }

    @Override
    public FestivalPage findBy(FestivalFilter filter, List<School> schools, FestivalPageable page,
                               LocalDate currentTime) {
        return v1FestivalRepositoryCustom.findBy(filter, schools, page, currentTime);
    }
}
