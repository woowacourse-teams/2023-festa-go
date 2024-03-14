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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ArtistFestivalSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    public ArtistFestivalSearchV1QueryDslRepository() {
        super(Artist.class);
    }

    public List<FestivalSearchV1Response> executeSearch(String keyword) {
        int keywordLength = keyword.length();
        if (keywordLength == 0) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }
        if (keywordLength == 1) {
            return searchByEqual(keyword);
        }
        return searchByLike(keyword);
    }

    private List<FestivalSearchV1Response> searchByEqual(String keyword) {
        return searchByExpression(artist.name.eq(keyword), keyword);
    }

    private List<FestivalSearchV1Response> searchByExpression(BooleanExpression expression, String keyword) {
        List<FestivalSearchV1Response> response = select(
            new QFestivalSearchV1Response(
                festival.id,
                festival.name,
                festival.startDate,
                festival.endDate,
                festival.thumbnail,
                festivalQueryInfo.artistInfo))
            .from(artist)
            .innerJoin(stageArtist).on(expression.and(stageArtist.artistId.eq(artist.id)))
            .innerJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .innerJoin(festival).on(festival.id.eq(stage.festival.id))
            .innerJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
            .where(expression)
            .fetch();

        if (response.size() > 5) {
            log.warn("{} 키워드로 검색시 5개를 초과한 검색 결과를 반환하였습니다", keyword);
        }

        return response;
    }

    private List<FestivalSearchV1Response> searchByLike(String keyword) {
        return searchByExpression(artist.name.contains(keyword), keyword);
    }
}
