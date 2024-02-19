package com.festago.artist.application;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.stage.domain.QStageArtist.stageArtist;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.artist.dto.QArtistDetailV1Response;
import com.festago.artist.dto.QArtistFestivalDetailV1Response;
import com.festago.artist.dto.QArtistMediaV1Response;
import com.festago.artist.repository.ArtistFestivalSearchCondition;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistDetailV1QueryDslRepository extends QueryDslRepositorySupport {

    private static final int NEXT_PAGE_DATA_TEMP_COUNT = 1;

    public ArtistDetailV1QueryDslRepository() {
        super(Artist.class);
    }

    public Optional<ArtistDetailV1Response> findArtistDetail(Long artistId) {
        List<ArtistDetailV1Response> response = selectFrom(artist)
            .leftJoin(socialMedia).on(socialMedia.ownerId.eq(artist.id).and(socialMedia.ownerType.eq(OwnerType.ARTIST)))
            .where(artist.id.eq(artistId))
            .transform(
                groupBy(artist.id).list(
                    new QArtistDetailV1Response(
                        artist.id,
                        artist.name,
                        artist.profileImage,
                        artist.backgroundImageUrl,
                        list(new QArtistMediaV1Response(
                            socialMedia.mediaType.stringValue(),
                            socialMedia.name,
                            socialMedia.logoUrl,
                            socialMedia.url
                        ).skipNulls())
                    )
                )
            );

        if (response.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.get(0));
    }

    public Slice<ArtistFestivalDetailV1Response> findArtistFestivals(ArtistFestivalSearchCondition condition) {
        List<ArtistFestivalDetailV1Response> response =
            selectArtistDetailResponse(condition.artistId())
                .where(getDynamicWhere(condition.isPast(), condition.lastStartDate(), condition.lastFestivalId(),
                    condition.currentTime()))
                .limit(condition.pageable().getPageSize() + NEXT_PAGE_DATA_TEMP_COUNT)
                .orderBy(getDynamicOrderBy(condition.isPast()))
                .fetch();

        return makeResponse(condition, response);
    }

    private SliceImpl<ArtistFestivalDetailV1Response> makeResponse(
        ArtistFestivalSearchCondition condition, List<ArtistFestivalDetailV1Response> content) {
        Pageable pageable = condition.pageable();
        if (content.size() > pageable.getPageSize()) {
            removeTemporaryContent(content);
            return new SliceImpl<>(content, pageable, true);
        }
        return new SliceImpl<>(content, condition.pageable(), false);
    }

    private void removeTemporaryContent(List<ArtistFestivalDetailV1Response> content) {
        content.remove(content.size() - NEXT_PAGE_DATA_TEMP_COUNT);
    }

    private JPAQuery<ArtistFestivalDetailV1Response> selectArtistDetailResponse(Long artistId) {
        return select(
            new QArtistFestivalDetailV1Response(
                festival.id,
                festival.name,
                festival.startDate,
                festival.endDate,
                festival.thumbnail,
                festivalQueryInfo.artistInfo))
            .from(stageArtist)
            .innerJoin(stage).on(stageArtist.artistId.eq(artistId).and(stage.id.eq(stageArtist.stageId)))
            .innerJoin(festival).on(festival.id.eq(stage.festival.id))
            .leftJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId));
    }

    private BooleanExpression getDynamicWhere(
        Boolean isPast,
        LocalDate lastStartDate,
        Long lastFestivalId,
        LocalDate currentTime
    ) {
        if (hasCursor(lastStartDate, lastFestivalId)) {
            return getCursorBasedWhere(isPast, lastStartDate, lastFestivalId);
        }
        return getDefaultWhere(isPast, currentTime);
    }

    private boolean hasCursor(LocalDate lastStartDate, Long lastFestivalId) {
        return lastStartDate != null && lastFestivalId != null;
    }

    private BooleanExpression getCursorBasedWhere(boolean isPast, LocalDate lastStartDate, Long lastFestivalId) {
        if (isPast) {
            return festival.startDate.lt(lastStartDate)
                .or(festival.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));
        }
        return festival.startDate.gt(lastStartDate)
            .or(festival.startDate.eq(lastStartDate)
                .and(festival.id.gt(lastFestivalId)));
    }

    private BooleanExpression getDefaultWhere(boolean isPast, LocalDate currentTime) {
        if (isPast) {
            return festival.endDate.lt(currentTime);
        }
        return festival.startDate.goe(currentTime).or(festival.endDate.goe(currentTime));
    }

    private OrderSpecifier<LocalDate>[] getDynamicOrderBy(Boolean isPast) {
        if (isPast) {
            return new OrderSpecifier[]{festival.endDate.desc()};
        }
        return new OrderSpecifier[]{festival.startDate.asc(), festival.id.asc()};
    }
}
