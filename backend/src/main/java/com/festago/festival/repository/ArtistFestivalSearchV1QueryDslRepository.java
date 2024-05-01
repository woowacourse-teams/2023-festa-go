package com.festago.festival.repository;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;

import com.festago.artist.domain.Artist;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.dto.QFestivalSearchV1Response;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistFestivalSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    public ArtistFestivalSearchV1QueryDslRepository() {
        super(Artist.class);
    }

    public List<FestivalSearchV1Response> executeSearch(String keyword) {
        return searchByExpression(getBooleanExpressionByKeyword(keyword));
    }

    private BooleanExpression getBooleanExpressionByKeyword(String keyword) {
        int keywordLength = keyword.length();
        if (keywordLength == 0) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }
        if (keywordLength == 1) {
            return artist.name.eq(keyword);
        }
        return artist.name.contains(keyword);
    }

    private List<FestivalSearchV1Response> searchByExpression(BooleanExpression expression) {
        return select(
            new QFestivalSearchV1Response(
                festival.id,
                festival.name,
                festival.festivalDuration.startDate,
                festival.festivalDuration.endDate,
                festival.posterImageUrl,
                festivalQueryInfo.artistInfo))
            .from(artist)
            .innerJoin(stageArtist).on(expression.and(stageArtist.artistId.eq(artist.id)))
            .innerJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .innerJoin(festival).on(festival.id.eq(stage.festival.id))
            .innerJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
            .where(expression)
            .fetch();
    }

    public boolean existsByName(String keyword) {
        return existsByExpression(getBooleanExpressionByKeyword(keyword));
    }

    private boolean existsByExpression(BooleanExpression expression) {
        return !selectFrom(artist)
            .where(expression)
            .fetch()
            .isEmpty();
    }
}
