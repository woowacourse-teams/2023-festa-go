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

    public SchoolDetailV1Response findById(Long schoolId) {
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

    public List<SchoolFestivalResponse> findCurrentFestivalBySchoolId(Long schoolId, LocalDate today, int size,
                                                                      Long lastFestivalId, LocalDate lastStartDate,
                                                                      boolean isPast) {
        return select(new QSchoolFestivalResponse(festival.id,
            festival.name,
            festival.startDate,
            festival.endDate,
            festival.thumbnail,
            festivalQueryInfo.artistInfo)
        )
            .from(festival)
            .where(school.id.eq(schoolId), addPagingOption(lastFestivalId, lastStartDate),
                addPhaseOption(isPast, today))
            .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .orderBy(addOrderOption(isPast))
            .limit(size + 1)
            .fetch();
    }

    private BooleanExpression addPagingOption(Long lastFestivalId, LocalDate lastStartDate) {
        return null;
    }

    private BooleanExpression addPhaseOption(boolean isPast, LocalDate today) {
        if (isPast) {
            return festival.endDate.lt(today);
        }

        return festival.startDate.goe(today);
    }

    private OrderSpecifier<LocalDate>[] addOrderOption(boolean isPast) {
        if (isPast) {
            return new OrderSpecifier[]{festival.endDate.desc()};
        }

        return new OrderSpecifier[]{festival.startDate.asc()};
    }
}
