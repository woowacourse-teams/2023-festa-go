package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.school.domain.QSchool.school;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.QFestivalV1Response;
import com.festago.festival.dto.QSchoolV1Response;
import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class FestivalV1QueryDslRepository extends QueryDslRepositorySupport {

    public FestivalV1QueryDslRepository() {
        super(Festival.class);
    }

    public Slice<FestivalV1Response> findBy(FestivalSearchCondition searchCondition) {
        FestivalFilter filter = searchCondition.filter();
        Pageable pageable = searchCondition.pageable();
        return applySlice(
            pageable,
            query -> query.select(new QFestivalV1Response(
                    festival.id,
                    festival.name,
                    festival.festivalDuration.startDate,
                    festival.festivalDuration.endDate,
                    festival.posterImageUrl,
                    new QSchoolV1Response(
                        school.id,
                        school.name
                    ),
                    festivalQueryInfo.artistInfo
                ))
                .from(festival)
                .innerJoin(school).on(school.id.eq(festival.school.id))
                .innerJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
                .where(dynamicWhere(filter, searchCondition.currentTime(), searchCondition.lastFestivalId(),
                    searchCondition.lastStartDate(), searchCondition.region()))
                .orderBy(dynamicOrderBy(filter))
        );
    }

    private BooleanExpression dynamicWhere(
        FestivalFilter filter,
        LocalDate currentTime,
        Long lastFestivalId,
        LocalDate lastStartDate,
        SchoolRegion region
    ) {
        BooleanExpression booleanExpression = getBooleanExpression(filter, currentTime, lastFestivalId, lastStartDate);
        booleanExpression = applyRegion(booleanExpression, region);
        return booleanExpression;
    }

    private BooleanExpression getBooleanExpression(
        FestivalFilter filter,
        LocalDate currentTime,
        Long lastFestivalId,
        LocalDate lastStartDate
    ) {
        if (hasCursor(lastStartDate, lastFestivalId)) {
            return getCursorBasedBooleanExpression(filter, currentTime, lastFestivalId, lastStartDate);
        }
        return getDefaultBooleanExpression(filter, currentTime);
    }

    private boolean hasCursor(LocalDate lastStartDate, Long lastFestivalId) {
        return lastStartDate != null && lastFestivalId != null;
    }

    private BooleanExpression getCursorBasedBooleanExpression(
        FestivalFilter filter,
        LocalDate currentTime,
        Long lastFestivalId,
        LocalDate lastStartDate
    ) {
        return switch (filter) {
            case PLANNED -> festival.festivalDuration.startDate.gt(lastStartDate)
                .or(festival.festivalDuration.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));

            case PROGRESS -> festival.festivalDuration.startDate.lt(lastStartDate)
                .or(festival.festivalDuration.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)))
                .and(festival.festivalDuration.endDate.goe(currentTime));

            case END -> festival.festivalDuration.endDate.lt(currentTime);
        };
    }

    private BooleanExpression getDefaultBooleanExpression(
        FestivalFilter filter,
        LocalDate currentTime
    ) {
        return switch (filter) {
            case PLANNED -> festival.festivalDuration.startDate.gt(currentTime);

            case PROGRESS -> festival.festivalDuration.startDate.loe(currentTime)
                .and(festival.festivalDuration.endDate.goe(currentTime));

            case END -> festival.festivalDuration.endDate.lt(currentTime);
        };
    }

    private BooleanExpression applyRegion(BooleanExpression booleanExpression, SchoolRegion region) {
        if (region == SchoolRegion.ANY) {
            return booleanExpression;
        }
        return booleanExpression.and(school.region.eq(region));
    }

    private OrderSpecifier<LocalDate>[] dynamicOrderBy(FestivalFilter filter) {
        return switch (filter) {
            case PLANNED -> new OrderSpecifier[]{festival.festivalDuration.startDate.asc(), festival.id.asc()};
            case PROGRESS -> new OrderSpecifier[]{festival.festivalDuration.startDate.desc(), festival.id.asc()};
            case END -> new OrderSpecifier[]{festival.festivalDuration.endDate.desc()};
        };
    }
}
