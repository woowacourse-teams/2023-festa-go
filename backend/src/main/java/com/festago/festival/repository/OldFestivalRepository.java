package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;

import com.festago.festival.domain.Festival;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OldFestivalRepository {

    private final JPAQueryFactory queryFactory;

    public List<Festival> findByFilter(FestivalFilter filter, LocalDate currentTime) {
        return switch (filter) {
            case PLANNED -> plannedFestivals(currentTime);
            case PROGRESS -> progressFestivals(currentTime);
            case END -> endFestivals(currentTime);
        };
    }

    private List<Festival> plannedFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival)
            .where(festival.startDate.gt(currentTime))
            .orderBy(festival.startDate.asc()).fetch();
    }

    private List<Festival> progressFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival)
            .where(festival.startDate.loe(currentTime).and(festival.endDate.goe(currentTime)))
            .orderBy(festival.startDate.asc()).fetch();
    }

    private List<Festival> endFestivals(LocalDate currentTime) {
        return queryFactory.selectFrom(festival)
            .where(festival.endDate.lt(currentTime))
            .orderBy(festival.endDate.desc()).fetch();
    }
}
