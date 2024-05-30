package com.festago.stage.infrastructure;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;

import com.festago.artist.domain.Artist;
import com.festago.common.querydsl.QueryDslHelper;
import com.festago.festival.domain.FestivalIdStageArtistsResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FestivalIdStageArtistsQueryDslResolver implements FestivalIdStageArtistsResolver {

    private final QueryDslHelper queryDslHelper;

    @Override
    public List<Artist> resolve(Long festivalId) {
        return queryDslHelper.select(artist)
            .from(festival)
            .join(stage).on(stage.festival.id.eq(festival.id))
            .join(stageArtist).on(stageArtist.stageId.eq(stage.id))
            .join(artist).on(artist.id.eq(stageArtist.artistId))
            .where(festival.id.eq(festivalId))
            .orderBy(artist.id.asc())
            .distinct()
            .fetch();
    }
}
