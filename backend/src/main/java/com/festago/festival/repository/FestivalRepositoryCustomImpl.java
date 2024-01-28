package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalV1Response;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
    public Slice<FestivalV1Response> findBy(FestivalSearchCondition searchCondition) {
        return festivalV1QueryDslRepositoryCustom.findBy(searchCondition);
    }
}
