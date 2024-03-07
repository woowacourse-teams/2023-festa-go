package com.festago.artist.repository;


import static com.festago.artist.domain.QArtist.artist;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.dto.QArtistSearchResponse;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.querydsl.core.group.GroupBy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistV1SearchQueryDslRepository extends QueryDslRepositorySupport {

    public ArtistV1SearchQueryDslRepository() {
        super(Artist.class);
    }

//    public void merge() {
//        List<Long> artistIds = List.of();
//        LocalDate today = LocalDate.now();
//        Map<Long, List<LocalDateTime>> artistToStartTime = findAllSomething(artistIds, today);
//        for (Long artistId : artistIds) {
//            int todayStage = 0;
//            int plannedStage = 0;
//            if (artistToStartTime.containsKey(artistId)) {
//                List<LocalDateTime> startTimes = artistToStartTime.get(artistId);
//                for (LocalDateTime startTime : startTimes) {
//                    if (startTime.toLocalDate().equals(today)) {
//                        todayStage++;
//                    } else {
//                        plannedStage++;
//                    }
//                }
//            }
//            //TODO set
//        }
//    }

    public Map<Long, List<LocalDateTime>> findArtistsStageStartTimeAfterDate(List<Long> artistIds, LocalDate today) {
        LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIN);
        Map<Long, List<LocalDateTime>> result = selectFrom(stageArtist)
            .leftJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .where(stageArtist.artistId.in(artistIds)
                .and(stage.startTime.goe(todayMidnight)))
            .transform(groupBy(stageArtist.artistId).as(GroupBy.list(stage.startTime)));
        for (Long artistId : artistIds) {
            if (!result.containsKey(artistId)) {
                result.put(artistId, Collections.emptyList());
            }
        }

        return result;
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
}
