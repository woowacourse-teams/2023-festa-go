package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageQueryInfo.stageQueryInfo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.sortedSet;

import com.festago.common.exception.UnexpectedException;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalDetailV1Response;
import com.festago.festival.dto.QFestivalDetailV1Response;
import com.festago.festival.dto.QSchoolV1Response;
import com.festago.festival.dto.QSocialMediaV1Response;
import com.festago.festival.dto.QStageV1Response;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.dto.StageV1Response;
import com.festago.socialmedia.domain.OwnerType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class FestivalDetailV1QueryDslRepository extends QueryDslRepositorySupport {

    public FestivalDetailV1QueryDslRepository() {
        super(Festival.class);
    }

    public Optional<FestivalDetailV1Response> findFestivalDetail(Long festivalId) {
        List<FestivalDetailV1Response> response = selectFrom(festival)
            .innerJoin(school).on(school.id.eq(festival.school.id))
            .leftJoin(socialMedia).on(socialMedia.ownerId.eq(school.id).and(socialMedia.ownerType.eq(OwnerType.SCHOOL)))
            .leftJoin(stage).on(stage.festival.id.eq(festival.id))
            .leftJoin(stageQueryInfo).on(stageQueryInfo.stageId.eq(stage.id))
            .where(festival.id.eq(festivalId))
            .transform(
                groupBy(festival.id).list(
                    new QFestivalDetailV1Response(
                        festival.id,
                        festival.name,
                        new QSchoolV1Response(
                            school.id,
                            school.name
                        ),
                        festival.startDate,
                        festival.endDate,
                        festival.thumbnail,
                        sortedSet(new QSocialMediaV1Response(
                            socialMedia.mediaType,
                            socialMedia.name,
                            socialMedia.logoUrl,
                            socialMedia.url
                        ).skipNulls(), Comparator.comparing(SocialMediaV1Response::name)),
                        sortedSet(new QStageV1Response(
                            stage.id,
                            stage.startTime,
                            stageQueryInfo.artistInfo
                        ).skipNulls(), Comparator.comparingLong(StageV1Response::id))
                    )
                )
            );
        if (response.isEmpty()) {
            return Optional.empty();
        }
        // PK로 조회하기에 발생할 일이 없는 예외지만, 혹시 모를 상황을 방지하기 위함
        if (response.size() >= 2) {
            throw new UnexpectedException("축제 상세 조회에서 2개 이상의 축제가 조회되었습니다.");
        }
        return Optional.of(response.get(0));
    }
}
