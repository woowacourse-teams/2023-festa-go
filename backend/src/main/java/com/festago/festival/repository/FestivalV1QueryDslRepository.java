package com.festago.festival.repository;


import static com.festago.festival.domain.QFestival.festival;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FestivalV1QueryDslRepository {

    private static final int INDEX_OFFSET = 1;

    private final JPAQueryFactory queryFactory;

    public FestivalPage findBy(FestivalFilter filter, FestivalPageable page, LocalDate currentTime) {
        return switch (filter) {
            case PLANNED -> plannedFestivals(page, currentTime);
            case PROGRESS -> progressFestivals(page, currentTime);
            case END -> throw new UnsupportedOperationException();
        };
    }

    private FestivalPage plannedFestivals(FestivalPageable page, LocalDate currentTime) {
        List<Festival> allPlannedFestivals = queryFactory.selectFrom(festival)
            .leftJoin(festival.school).fetchJoin()
            .where(festival.startDate.gt(currentTime))
            .orderBy(festival.startDate.asc(), festival.id.asc())
            .fetch();
        return pagingFestival(page, allPlannedFestivals);
    }

    private FestivalPage pagingFestival(FestivalPageable page, List<Festival> allFestivals) {
        if (page.getLastFestivalId().isPresent() && page.getLastFestivalId().isPresent()) {
            return pagedFestival(page, allFestivals);
        }
        int festivalSize = allFestivals.size();
        int limit = page.getLimit();
        int returnFestivalSize = Math.min(festivalSize, limit);
        return makePage(festivalSize - returnFestivalSize, allFestivals.subList(0, returnFestivalSize));
    }

    private FestivalPage pagedFestival(FestivalPageable page, List<Festival> allFestivals) {
        Long lastFestivalId = page.getLastFestivalId().get();
        Festival lastFestival = findLastFestival(allFestivals, lastFestivalId);
        int targetFestivalIndex = allFestivals.indexOf(lastFestival);
        int allFestivalSize = allFestivals.size();
        int festivalOrder = targetFestivalIndex + INDEX_OFFSET;
        if (allFestivalSize > festivalOrder) {
            int remainFestivalSize = allFestivalSize - festivalOrder;
            int limit = page.getLimit();
            int returnFestivalSize = Math.min(remainFestivalSize, limit);
            int startIndex = targetFestivalIndex + INDEX_OFFSET;
            return makePage(remainFestivalSize - returnFestivalSize,
                allFestivals.subList(startIndex, startIndex + returnFestivalSize));
        }
        return new FestivalPage(true, Collections.emptyList());
    }

    private Festival findLastFestival(List<Festival> festivals, Long lastFestivalId) {
        return festivals.stream()
            .filter(festival -> festival.getId().equals(lastFestivalId))
            .findAny()
            .orElseThrow(() -> new BadRequestException(ErrorCode.FESTIVAL_NOT_FOUND));
    }

    private FestivalPage makePage(int remainFestivalSize, List<Festival> festivals) {
        if (remainFestivalSize > 0) {
            return new FestivalPage(false, festivals);
        }
        return new FestivalPage(true, festivals);
    }

    private FestivalPage progressFestivals(FestivalPageable page, LocalDate currentTime) {
        List<Festival> allProgressFestival = findSortedProgressFestivals(currentTime);
        return pagingFestival(page, allProgressFestival);
    }

    private List<Festival> findSortedProgressFestivals(LocalDate currentTime) {
        return progressQuery(currentTime)
            .orderBy(festival.startDate.desc(), festival.id.asc())
            .fetch();
    }

    private JPAQuery<Festival> progressQuery(LocalDate currentTime) {
        return queryFactory.selectFrom(festival)
            .leftJoin(festival.school).fetchJoin()
            .where(festival.startDate.loe(currentTime)
                .and(festival.endDate.goe(currentTime)));
    }

    public FestivalPage findBy(FestivalFilter filter, List<School> schools, FestivalPageable page,
                               LocalDate currentTime) {
        return switch (filter) {
            case PLANNED -> plannedFestivals(page, schools, currentTime);
            case PROGRESS -> progressFestivals(page, schools, currentTime);
            case END -> throw new UnsupportedOperationException();
        };
    }

    private FestivalPage plannedFestivals(FestivalPageable page, List<School> schools, LocalDate currentTime) {
        List<Long> schoolIds = extractSchoolId(schools);
        List<Festival> allPlannedFestivals = queryFactory.selectFrom(festival)
            .leftJoin(festival.school).fetchJoin()
            .where(festival.startDate.gt(currentTime)
                .and(festival.school.id.in(schoolIds)))
            .orderBy(festival.startDate.asc(), festival.id.asc())
            .fetch();
        return pagingFestival(page, allPlannedFestivals);
    }

    private FestivalPage progressFestivals(FestivalPageable page, List<School> schools, LocalDate currentTime) {
        List<Festival> allProgressFestivals = findSortedProgressFestivals(schools, currentTime);
        return pagingFestival(page, allProgressFestivals);
    }

    private List<Festival> findSortedProgressFestivals(List<School> schools, LocalDate currentTime) {
        List<Long> schoolIds = extractSchoolId(schools);
        return progressQuery(currentTime)
            .where(festival.school.id.in(schoolIds))
            .orderBy(festival.startDate.desc(), festival.id.asc())
            .fetch();
    }

    private List<Long> extractSchoolId(List<School> schools) {
        return schools.stream()
            .map(School::getId)
            .toList();
    }
}
