package com.festago.artist.repository;


import static com.festago.artist.domain.QArtist.artist;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.dto.ArtistSearchStageCount;
import com.festago.artist.dto.QArtistSearchResponse;
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

    public List<ArtistSearchResponse> findAllByLike(String keyword) {
        return select(
            new QArtistSearchResponse(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.contains(keyword))
            .fetch();
    }

    public List<ArtistSearchResponse> findAllByEqual(String keyword) {
        return select(
            new QArtistSearchResponse(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.eq(keyword))
            .fetch();
    }

    public Map<Long, ArtistSearchStageCount> findArtistsStageScheduleAfterDateTime(List<Long> artistIds, LocalDateTime localDateTime) {
        Map<Long, List<LocalDateTime>> result = selectFrom(stageArtist)
            .leftJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .where(stageArtist.artistId.in(artistIds)
                .and(stage.startTime.goe(localDateTime)))
            .transform(groupBy(stageArtist.artistId).as(GroupBy.list(stage.startTime)));
        return getStageCountResult(artistIds, localDateTime.toLocalDate(), result);
    }

    private Map<Long, ArtistSearchStageCount> getStageCountResult(List<Long> artistIds,
                                                                  LocalDate today,
                                                                  Map<Long, List<LocalDateTime>> artistToStageStartTimes) {
        Map<Long, ArtistSearchStageCount> result = new HashMap<>();
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
            result.put(artistId, new ArtistSearchStageCount(todayStageCount, plannedStageCount));
        }
        return result;
    }
}
