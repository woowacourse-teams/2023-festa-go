package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.Festival;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FestivalRepositoryCustomImpl implements FestivalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Festival> findFestivalBy(FestivalFilter festivalFilter, LocalDate currentTime) {
        return switch (festivalFilter) {
            case PLANNED -> plannedFestivals(currentTime);
            case PROGRESS -> progressFestivals(currentTime);
            case END -> endFestivals(currentTime);
            default -> throw new InternalServerException(ErrorCode.NOT_DEFINED_FESTIVAL_FESTIVAL);
        };
    }

    private List<Festival> plannedFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival).where(festival.startDate.gt(currentTime))
            .orderBy(festival.startDate.asc()).fetch();
    }

    private List<Festival> progressFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival)
            .where(festival.startDate.loe(currentTime).and(festival.endDate.goe(currentTime)))
            .orderBy(festival.startDate.asc()).fetch();
    }

    private List<Festival> endFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival).where(festival.endDate.lt(currentTime))
            .orderBy(festival.endDate.desc()).fetch();
    }
}
