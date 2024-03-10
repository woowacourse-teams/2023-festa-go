package com.festago.artist.repository;


import static com.festago.artist.domain.QArtist.artist;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.dto.QArtistSearchV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.querydsl.core.group.GroupBy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    public Map<Long, ArtistSearchStageCountV1Response> findArtistsStageScheduleAfterDateTime(List<Long> artistIds, LocalDateTime localDateTime) {
        Map<Long, List<LocalDateTime>> result = selectFrom(stageArtist)
            .leftJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .where(stageArtist.artistId.in(artistIds)
                .and(stage.startTime.goe(localDateTime)))
            .transform(groupBy(stageArtist.artistId).as(GroupBy.list(stage.startTime)));
        return getStageCountResponse(artistIds, localDateTime.toLocalDate(), result);
    }

    private Map<Long, ArtistSearchStageCountV1Response> getStageCountResponse(List<Long> artistIds,
                                                                              LocalDate today,
                                                                              Map<Long, List<LocalDateTime>> artistToStageStartTimes) {
        Map<Long, ArtistSearchStageCountV1Response> result = new HashMap<>();
        for (Long artistId : artistIds) {
            int todayStageCount = 0;
            int plannedStageCount = 0;
            if (artistToStageStartTimes.containsKey(artistId)) {
                for (LocalDateTime startTime : artistToStageStartTimes.get(artistId)) {
                    if (startTime.toLocalDate().equals(today)) {
                        todayStageCount++;
                    } else {
                        plannedStageCount++;
                    }
                }
            }
            result.put(artistId, new ArtistSearchStageCountV1Response(todayStageCount, plannedStageCount));
        }
        return result;
    }
}
