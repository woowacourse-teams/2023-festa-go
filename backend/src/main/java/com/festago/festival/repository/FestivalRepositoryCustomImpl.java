package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalRepositoryCustomImpl implements FestivalRepositoryCustom {

    private final OldFestivalRepository oldFestivalRepositoryCustom;

    private final FestivalV1QueryDslRepository festivalV1QueryDslRepositoryCustom;

    @Override
    public List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime) {
        return oldFestivalRepositoryCustom.findByFilter(filter, currentTime);
    }

    @Override
    public FestivalPage findBy(FestivalFilter filter, FestivalPageable page, LocalDate currentTime) {
        return festivalV1QueryDslRepositoryCustom.findBy(filter, page, currentTime);
    }

    @Override
    public FestivalPage findBy(FestivalFilter filter, List<School> schools, FestivalPageable page,
                               LocalDate currentTime) {
        return festivalV1QueryDslRepositoryCustom.findBy(filter, schools, page, currentTime);
    }
}
