package com.festago.school.repository.v1;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.QSchoolDetailV1Response;
import com.festago.school.dto.v1.QSchoolFestivalV1Response;
import com.festago.school.dto.v1.QSchoolSocialMediaV1Response;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.dto.v1.SliceResponse;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolV1QueryDslRepository extends QueryDslRepositorySupport {

    private static final int NEXT_PAGE_CHECK_NUMBER = 1;

    public SchoolV1QueryDslRepository() {
        super(School.class);
    }

    public SchoolDetailV1Response findDetailById(Long schoolId) {
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
            throw new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        return response.get(0);
    }

    public SliceResponse<SchoolFestivalV1Response> findFestivalsBySchoolId(Long schoolId, LocalDate today,
                                                                           SchoolFestivalV1SearchCondition searchCondition) {
        List<SchoolFestivalV1Response> result =
            select(new QSchoolFestivalV1Response(festival.id,
                    festival.name,
                    festival.startDate,
                    festival.endDate,
                    festival.thumbnail,
                    festivalQueryInfo.artistInfo
                )
            )
                .from(festival)
                .where(festival.school.id.eq(schoolId),
                    addPhaseOption(searchCondition.isPast(), today),
                    addPagingOption(searchCondition.lastFestivalId(), searchCondition.lastStartDate(),
                        searchCondition.isPast()))
                .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
                .orderBy(addOrderOption(searchCondition.isPast()))
                .limit(searchCondition.size() + NEXT_PAGE_CHECK_NUMBER)
                .fetch();

        return createResponse(searchCondition.size(), result);
    }

    private BooleanExpression addPhaseOption(boolean isPast, LocalDate today) {
        if (isPast) {
            return festival.endDate.lt(today);
        }

        return festival.endDate.goe(today);
    }

    private BooleanExpression addPagingOption(Long lastFestivalId, LocalDate lastStartDate, boolean isPast) {
        if (isNotFirstPage(lastFestivalId, lastStartDate)) {
            if (isPast) {
                return festival.startDate.lt(lastStartDate)
                    .or(festival.startDate.eq(lastStartDate)
                        .and(festival.id.gt(lastFestivalId)));
            }
            return festival.startDate.gt(lastStartDate)
                .or(festival.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));
        }

        return null;
    }

    private boolean isNotFirstPage(Long lastFestivalId, LocalDate lastStartDate) {
        return lastFestivalId != null && lastStartDate != null;
    }

    private OrderSpecifier<LocalDate>[] addOrderOption(boolean isPast) {
        if (isPast) {
            return new OrderSpecifier[]{festival.endDate.desc()};
        }

        return new OrderSpecifier[]{festival.startDate.asc()};
    }

    private SliceResponse<SchoolFestivalV1Response> createResponse(
        Integer pageSize,
        List<SchoolFestivalV1Response> content) {
        boolean isLast = true;
        if (content.size() == pageSize + NEXT_PAGE_CHECK_NUMBER) {
            content.remove(content.size() - NEXT_PAGE_CHECK_NUMBER);
            isLast = false;
        }

        return new SliceResponse(isLast, content);
    }
}
