package com.festago.festival.repository;


import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalInfo.festivalInfo;
import static com.festago.school.domain.QSchool.school;

import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.QFestivalV1Response;
import com.festago.festival.dto.QSchoolV1Response;
import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FestivalV1QueryDslRepository {

    private static final long NEXT_PAGE_DATA = 1;

    private final JPAQueryFactory queryFactory;

    public Slice<FestivalV1Response> findBy(FestivalFilter filter, SchoolRegion region, FestivalPageable page,
                                            LocalDate currentTime) {
        List<FestivalV1Response> content = selectResponse()
            .where(dynamicWhere(filter, currentTime, page.getLastFestivalId(), page.getLastStartDate(), region))
            .orderBy(dynamicOrderBy(filter))
            .limit(page.getLimit() + NEXT_PAGE_DATA)
            .fetch();

        return new SliceImpl<>(content, PageRequest.of(1, 1), hasNext(content, page.getLimit()));
    }

    private JPAQuery<FestivalV1Response> selectResponse() {
        return queryFactory.select(new QFestivalV1Response(
                festival.id,
                festival.name,
                festival.startDate,
                festival.endDate,
                festival.thumbnail,
                new QSchoolV1Response(
                    school.id,
                    school.name,
                    school.region
                ),
                festivalInfo.artistInfo)
            )
            .from(festival)
            .innerJoin(school).on(school.id.eq(festival.school.id))
            .innerJoin(festivalInfo).on(festivalInfo.festivalId.eq(festival.id));
    }

    private BooleanExpression dynamicWhere(FestivalFilter filter, LocalDate currentTime, Long lastFestivalId,
                                           LocalDate lastStartDate,
                                           SchoolRegion region) {
        if (hasCursor(lastStartDate, lastFestivalId)) {
            return cursorBasedWhere(filter, currentTime, lastFestivalId, lastStartDate, region);
        }

        BooleanExpression filterResult = switch (filter) {
            case PLANNED -> festival.startDate.gt(currentTime);
            case PROGRESS -> festival.startDate.loe(currentTime).and(festival.endDate.goe(currentTime));
            case END -> festival.endDate.lt(currentTime);
        };
        return addRegion(filterResult, region);
    }

    private OrderSpecifier<LocalDate>[] dynamicOrderBy(FestivalFilter filter) {
        return switch (filter) {
            case PLANNED -> new OrderSpecifier[]{festival.startDate.asc(), festival.id.asc()};
            case PROGRESS -> new OrderSpecifier[]{festival.startDate.desc(), festival.id.asc()};
            case END -> new OrderSpecifier[]{festival.endDate.desc()};
        };
    }

    private boolean hasNext(List<FestivalV1Response> content, Integer limit) {
        return content.size() > limit;
    }

    private BooleanExpression cursorBasedWhere(FestivalFilter filter, LocalDate currentTime, Long lastFestivalId,
                                               LocalDate lastStartDate, SchoolRegion region) {
        BooleanExpression filterResult = switch (filter) {
            case PLANNED -> festival.startDate.gt(lastStartDate)
                .or(festival.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));
            case PROGRESS -> festival.startDate.lt(lastStartDate)
                .or(festival.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)))
                .and(festival.endDate.goe(currentTime));
            case END -> festival.endDate.lt(currentTime);
        };
        return addRegion(filterResult, region);
    }

    private BooleanExpression addRegion(BooleanExpression filterResult, SchoolRegion region) {
        if (region == null || region == SchoolRegion.기타) {
            return filterResult;
        }
        return filterResult.and(school.region.eq(region));
    }

    private boolean hasCursor(LocalDate lastStartDate, Long lastFestivalId) {
        return lastStartDate != null && lastFestivalId != null;
    }
}
