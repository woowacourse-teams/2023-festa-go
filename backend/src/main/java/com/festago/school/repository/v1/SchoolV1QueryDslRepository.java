package com.festago.school.repository.v1;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.QSchoolDetailV1Response;
import com.festago.school.dto.v1.QSchoolFestivalArtistResponse;
import com.festago.school.dto.v1.QSchoolSocialMediaV1Response;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalArtistResponse;
import com.festago.school.dto.v1.SchoolFestivalResponse;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
                                                                      Long lastFestivalId, LocalDate startDate) {
        List<Festival> festivals = getCurrentOrFutureFestivals(schoolId, today, size, lastFestivalId, startDate);
        List<Long> festivalIds = festivals.stream()
            .map(Festival::getId)
            .toList();
        Map<Long, List<SchoolFestivalArtistResponse>> artistResponses = getArtistResponses(festivalIds);

        return festivals.stream()
            .map(it -> SchoolFestivalResponse.of(it, artistResponses.get(it.getId())))
            .toList();
    }

    private List<Festival> getCurrentOrFutureFestivals(Long schoolId, LocalDate today, int size, Long lastFestivalId,
                                                       LocalDate startDate) {
        return selectFrom(festival)
            .where(festival.school.id.eq(schoolId)
                .and(festival.endDate.goe(today))
                .and(applyPage(lastFestivalId, startDate))
            )
            .orderBy(festival.startDate.asc())
            .limit(size + 1)
            .fetch();
    }

    private BooleanExpression applyPage(Long lastFestivalId, LocalDate lastStartDate) {
        if (lastStartDate == null || lastStartDate == null) {
            return null;
        }

        return festival.startDate.goe(lastStartDate)
                .and(festival.id.gt(lastFestivalId));
    }

    private Map<Long, List<SchoolFestivalArtistResponse>> getArtistResponses(List<Long> festivalIds) {
        return selectFrom(stage)
            .where(stage.festival.id.in(festivalIds))
            .leftJoin(stageArtist).on(stageArtist.stageId.eq(stage.id))
            .leftJoin(artist).on(artist.id.eq(stage.id))
            .transform(groupBy(stage.festival.id).as(
                list(new QSchoolFestivalArtistResponse(artist.id, artist.name, artist.profileImage))));
    }
}
