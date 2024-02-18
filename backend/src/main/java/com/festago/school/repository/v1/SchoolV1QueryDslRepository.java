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
import com.festago.school.dto.v1.QSchoolFestivalResponse;
import com.festago.school.dto.v1.QSchoolSocialMediaV1Response;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalResponse;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

//TODO: 커밋전 변경 클래스들 개행 수정
@Repository
public class SchoolV1QueryDslRepository extends QueryDslRepositorySupport {

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
                    new QSchoolDetailV1Response(
                        school.id, school.name, school.logoUrl, school.backgroundUrl,
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

    public List<SchoolFestivalResponse> findFestivalsBySchoolId(Long schoolId, LocalDate today, int size,
                                                                Long lastFestivalId, LocalDate lastStartDate,
                                                                boolean isPast) {
        List<SchoolFestivalResponse> result = select(new QSchoolFestivalResponse(festival.id,
            festival.name,
            festival.startDate,
            festival.endDate,
            festival.thumbnail,
            festivalQueryInfo.artistInfo)
        )
            .from(festival)
            .where(festival.school.id.eq(schoolId),
                addPhaseOption(isPast, today),
                addPagingOption(lastFestivalId, lastStartDate, isPast))
            .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .orderBy(addOrderOption(isPast))
            .limit(size + 1)
            .fetch();
        if (result.size() > size) {
            result.remove(result.size() - 1);
        }

        return result;
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
}
