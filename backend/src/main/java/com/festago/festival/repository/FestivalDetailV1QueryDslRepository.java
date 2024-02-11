package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageQueryInfo.stageQueryInfo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalDetailV1Response;
import com.festago.festival.dto.SchoolV1Response;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.dto.StageV1Response;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.Projections;
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
            .innerJoin(stageQueryInfo).on(stageQueryInfo.stageId.eq(stage.id))
            .where(festival.id.eq(festivalId))
            .distinct()
            .transform(
                groupBy(festival.id).list(
                    Projections.constructor(
                        FestivalDetailV1Response.class,
                        festival.id,
                        festival.name,
                        Projections.constructor(
                            SchoolV1Response.class,
                            school.id,
                            school.name
                        ),
                        festival.startDate,
                        festival.endDate,
                        festival.thumbnail,
                        set(
                            Projections.constructor(
                                SocialMediaV1Response.class,
                                socialMedia.mediaType,
                                socialMedia.name,
                                socialMedia.logoUrl,
                                socialMedia.url
                            ).skipNulls()
                        ),
                        set(
                            Projections.constructor(
                                StageV1Response.class,
                                stage.id,
                                stage.startTime,
                                stageQueryInfo.artistInfo
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
}
