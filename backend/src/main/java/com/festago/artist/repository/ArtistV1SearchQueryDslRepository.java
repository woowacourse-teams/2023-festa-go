package com.festago.artist.repository;


import static com.festago.artist.domain.QArtist.artist;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.dto.QArtistSearchV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.querydsl.core.group.GroupBy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistV1SearchQueryDslRepository extends QueryDslRepositorySupport {

    public ArtistV1SearchQueryDslRepository() {
        super(Artist.class);
    }

    public List<ArtistSearchV1Response> findAllByLike(String keyword) {
        return select(
            new QArtistSearchV1Response(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.contains(keyword))
            .fetch();
    }

    public List<ArtistSearchV1Response> findAllByEqual(String keyword) {
        return select(
            new QArtistSearchV1Response(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.eq(keyword))
            .fetch();
    }

    public Map<Long, List<LocalDateTime>> findArtistsStageScheduleAfterDateTime(List<Long> artistIds, LocalDateTime localDateTime) {
        return selectFrom(stageArtist)
            .leftJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .where(stageArtist.artistId.in(artistIds)
                .and(stage.startTime.goe(localDateTime)))
            .transform(groupBy(stageArtist.artistId).as(GroupBy.list(stage.startTime)));
    }
}
