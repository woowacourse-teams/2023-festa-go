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
public class V1FestivalRepositoryCustomImpl {

    private static final int INDEX_OFFSET = 1;
    private static final int CHECK_LAST_FESTIVAL = 1;

    private final JPAQueryFactory queryFactory;

    public FestivalPage findBy(FestivalFilter filter, FestivalPageable page, LocalDate currentTime) {
        return switch (filter) {
            case PLANNED -> plannedFestivals(page, currentTime);
            case PROGRESS -> progressFestivals(page, currentTime);
            case END -> throw new UnsupportedOperationException();
        };
    }

    private FestivalPage plannedFestivals(FestivalPageable page, LocalDate currentTime) {
        if (page.getLastFestivalId().isPresent() && page.getLastFestivalId().isPresent()) {
            return pagedPlannedFestival(page, currentTime);
        }
        int limitSize = page.getLimit();
        List<Festival> plannedFestivals = findNPlannedQuery(currentTime, limitSize + CHECK_LAST_FESTIVAL);
        return determinePage(plannedFestivals, limitSize);
    }

    private FestivalPage pagedPlannedFestival(FestivalPageable page, LocalDate currentTime) {
        List<Festival> allPlannedFestival = queryFactory.selectFrom(festival)
            .leftJoin(festival.school).fetchJoin()
            .where(festival.startDate.gt(currentTime))
            .orderBy(festival.startDate.asc(), festival.id.asc())
            .fetch();
        return pagingFestival(page, allPlannedFestival);
    }

    private FestivalPage determinePage(List<Festival> remainSameDateFestivals, int limitSize) {
        if (remainSameDateFestivals.size() == limitSize + CHECK_LAST_FESTIVAL) {
            return new FestivalPage(false, remainSameDateFestivals);
        }
        return new FestivalPage(true, remainSameDateFestivals);
    }

    private List<Festival> findNPlannedQuery(LocalDate startDate, int limit) {
        return queryFactory.selectFrom(festival)
            .leftJoin(festival.school).fetchJoin()
            .where(festival.startDate.gt(startDate))
            .orderBy(festival.startDate.asc(), festival.id.asc())
            .limit(limit)
            .fetch();
    }

    private FestivalPage pagingFestival(FestivalPageable page, List<Festival> allFestivals) {
        Long lastFestivalId = page.getLastFestivalId().get();
        Festival lastFestival = findLastFestival(allFestivals, lastFestivalId);
        int targetFestivalIndex = allFestivals.indexOf(lastFestival);
        int allFestivalSize = allFestivals.size();
        int festivalOrder = targetFestivalIndex + INDEX_OFFSET;
        if (allFestivalSize > festivalOrder) {
            int remainFestivalSize = allFestivalSize - festivalOrder;
            int limit = page.getLimit();
            int returnFestivalSize = Math.min(remainFestivalSize, limit);
            return makePage(limit,
                allFestivals.subList(targetFestivalIndex, targetFestivalIndex + returnFestivalSize));
        }
        return new FestivalPage(true, Collections.emptyList());
    }

    private Festival findLastFestival(List<Festival> festivals, Long lastFestivalId) {
        return festivals.stream()
            .filter(festival -> festival.getId().equals(lastFestivalId))
            .findAny()
            .orElseThrow(() -> new BadRequestException(ErrorCode.FESTIVAL_NOT_FOUND));
    }

    private FestivalPage makePage(int limit, List<Festival> festivals) {
        if (festivals.size() < limit) {
            return new FestivalPage(true, festivals);
        }
        return new FestivalPage(false, festivals);
    }

    private FestivalPage progressFestivals(FestivalPageable page, LocalDate currentTime) {
        List<Festival> allProgressFestival = findSortedProgressFestivals(currentTime);
        return pagingFestival(page, allProgressFestival);
    }

    private List<Festival> findSortedProgressFestivals(LocalDate currentTime) {
        return progressQuery(currentTime)
            .orderBy(festival.startDate.desc())
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
            .orderBy(festival.startDate.desc())
            .fetch();
    }

    private List<Long> extractSchoolId(List<School> schools) {
        return schools.stream()
            .map(School::getId)
            .toList();
    }
}
