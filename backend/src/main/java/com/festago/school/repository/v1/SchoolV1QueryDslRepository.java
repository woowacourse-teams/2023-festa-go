package com.festago.school.repository.v1;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.QSchoolDetailV1Response;
import com.festago.school.dto.v1.QSchoolFestivalV1Response;
import com.festago.school.dto.v1.QSchoolSocialMediaV1Response;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolV1QueryDslRepository extends QueryDslRepositorySupport {

    public SchoolV1QueryDslRepository() {
        super(School.class);
    }

    public Optional<SchoolDetailV1Response> findDetailById(Long schoolId) {
        List<SchoolDetailV1Response> response = selectFrom(school)
            .where(school.id.eq(schoolId))
            .leftJoin(socialMedia).on(socialMedia.ownerId.eq(schoolId)
                .and(socialMedia.ownerType.eq(OwnerType.SCHOOL)))
            .transform(
                groupBy(school.id).list(
                    new QSchoolDetailV1Response(school.id, school.name, school.logoUrl, school.backgroundUrl,
                        list(
                            new QSchoolSocialMediaV1Response(
                                socialMedia.mediaType,
                                socialMedia.name,
                                socialMedia.logoUrl,
                                socialMedia.url
                            ).skipNulls()
                        )
                    )
                )
            );

        if (response.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.get(0));
    }

    public Slice<SchoolFestivalV1Response> findFestivalsBySchoolId(
        Long schoolId,
        LocalDate today,
        SchoolFestivalV1SearchCondition searchCondition
    ) {
        Pageable pageable = searchCondition.pageable();
        return applySlice(
            pageable,
            query -> query.select(new QSchoolFestivalV1Response(festival.id,
                        festival.name,
                        festival.festivalDuration.startDate,
                        festival.festivalDuration.endDate,
                        festival.posterImageUrl,
                        festivalQueryInfo.artistInfo
                    )
                )
                .from(festival)
                .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
                .where(festival.school.id.eq(schoolId),
                    addPhaseOption(searchCondition.isPast(), today),
                    addPagingOption(
                        searchCondition.lastFestivalId(),
                        searchCondition.lastStartDate(),
                        searchCondition.isPast()
                    ))
                .orderBy(addOrderOption(searchCondition.isPast()))
        );
    }

    private BooleanExpression addPhaseOption(boolean isPast, LocalDate today) {
        if (isPast) {
            return festival.festivalDuration.endDate.lt(today);
        }

        return festival.festivalDuration.endDate.goe(today);
    }

    private BooleanExpression addPagingOption(Long lastFestivalId, LocalDate lastStartDate, boolean isPast) {
        if (isNotFirstPage(lastFestivalId, lastStartDate)) {
            if (isPast) {
                return festival.festivalDuration.startDate.lt(lastStartDate)
                    .or(festival.festivalDuration.startDate.eq(lastStartDate)
                        .and(festival.id.gt(lastFestivalId)));
            }
            return festival.festivalDuration.startDate.gt(lastStartDate)
                .or(festival.festivalDuration.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));
        }
        return null;
    }

    private boolean isNotFirstPage(Long lastFestivalId, LocalDate lastStartDate) {
        return lastFestivalId != null && lastStartDate != null;
    }

    private OrderSpecifier<LocalDate> addOrderOption(boolean isPast) {
        if (isPast) {
            return festival.festivalDuration.endDate.desc();
        }
        return festival.festivalDuration.startDate.asc();
    }
}
