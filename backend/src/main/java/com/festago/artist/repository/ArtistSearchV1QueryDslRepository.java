package com.festago.artist.repository;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.dto.ArtistsSearchV1Response;
import com.festago.artist.dto.QArtistFestivalDetailV1Response;
import com.festago.artist.dto.QArtistSearchV1Response;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ArtistSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    public ArtistSearchV1QueryDslRepository() {
        super(Artist.class);
    }

    public Optional<ArtistsSearchV1Response> executeSearch(String keyword) {
        int keywordLength = keyword.length();
        if (keywordLength == 0) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }
        if (keywordLength == 1) {
            return searchByEqual(keyword);
        }
        return searchByLike(keyword);
    }

    private Optional<ArtistsSearchV1Response> searchByEqual(String keyword) {
        return searchByExpression(artist.name.eq(keyword), keyword);
    }

    private Optional<ArtistsSearchV1Response> searchByExpression(BooleanExpression expression, String keyword) {
        List<ArtistSearchV1Response> response = selectFrom(artist)
            .leftJoin(stageArtist).on(expression.and(stageArtist.artistId.eq(artist.id)))
            .leftJoin(stage).on(stage.id.eq(stageArtist.stageId))
            .leftJoin(festival).on(festival.id.eq(stage.festival.id))
            .leftJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
            .where(expression)
            .transform(
                groupBy(artist.id).list(
                    new QArtistSearchV1Response(
                        artist.id,
                        artist.name,
                        artist.profileImage,
                        list(new QArtistFestivalDetailV1Response(
                            festival.id,
                            festival.name,
                            festival.startDate,
                            festival.endDate,
                            festival.thumbnail,
                            festivalQueryInfo.artistInfo
                        ).skipNulls())
                    ).skipNulls()
                )
            );

        if (response.isEmpty()) {
            return Optional.empty();
        }

        if (response.size() > 5) {
            log.warn("{} 키워드로 검색시 3개를 초과한 검색 결괄르 반환하였습니다", keyword);
        }

        return Optional.of(new ArtistsSearchV1Response(response));
    }

    private Optional<ArtistsSearchV1Response> searchByLike(String keyword) {
        return searchByExpression(artist.name.contains(keyword), keyword);
    }
}
