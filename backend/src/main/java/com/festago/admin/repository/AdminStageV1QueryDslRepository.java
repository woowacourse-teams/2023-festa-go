package com.festago.admin.repository;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.admin.dto.stage.QAdminStageArtistV1Response;
import com.festago.admin.dto.stage.QAdminStageV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.stage.domain.Stage;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AdminStageV1QueryDslRepository extends QueryDslRepositorySupport {

    protected AdminStageV1QueryDslRepository() {
        super(Stage.class);
    }

    public List<AdminStageV1Response> findAllByFestivalId(Long festivalId) {
        return selectFrom(stage)
            .innerJoin(stageArtist).on(stageArtist.stageId.eq(stage.id))
            .innerJoin(artist).on(artist.id.eq(stageArtist.artistId))
            .where(stage.festival.id.eq(festivalId))
            .orderBy(stage.startTime.asc())
            .transform(
                groupBy(stage.id).list(
                    new QAdminStageV1Response(
                        stage.id,
                        stage.startTime,
                        stage.ticketOpenTime,
                        list(new QAdminStageArtistV1Response(
                            artist.id,
                            artist.name
                        )),
                        stage.createdAt,
                        stage.updatedAt
                    )
                )
            );
    }

    public Optional<AdminStageV1Response> findById(Long stageId) {
        List<AdminStageV1Response> response = selectFrom(stage)
            .leftJoin(stageArtist).on(stageArtist.stageId.eq(stageId))
            .leftJoin(artist).on(artist.id.eq(stageArtist.artistId))
            .where(stage.id.eq(stageId))
            .transform(
                groupBy(stage.id).list(
                    new QAdminStageV1Response(
                        stage.id,
                        stage.startTime,
                        stage.ticketOpenTime,
                        list(new QAdminStageArtistV1Response(
                            artist.id,
                            artist.name
                        )),
                        stage.createdAt,
                        stage.updatedAt
                    )
                )
            );
        if (response.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.get(0));
    }
}
