package com.festago.artist.repository;

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
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.socialmedia.domain.OwnerType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistDetailV1QueryDslRepository extends QueryDslRepositorySupport {

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
                        list(
                            new QArtistMediaV1Response(
                                socialMedia.mediaType.stringValue(),
                                socialMedia.name,
                                socialMedia.logoUrl,
                                socialMedia.url
                            ).skipNulls()
                        )
                    )
                )
            );

        if (response.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.get(0));
    }

    public Slice<ArtistFestivalDetailV1Response> findArtistFestivals(ArtistFestivalSearchCondition condition) {
        Pageable pageable = condition.pageable();
        Long artistId = condition.artistId();
        return applySlice(
            pageable,
            query -> query.select(new QArtistFestivalDetailV1Response(
                    festival.id,
                    festival.name,
                    festival.festivalDuration.startDate,
                    festival.festivalDuration.endDate,
                    festival.posterImageUrl,
                    festivalQueryInfo.artistInfo
                ))
                .from(stageArtist)
                .innerJoin(stage).on(stageArtist.artistId.eq(artistId).and(stage.id.eq(stageArtist.stageId)))
                .innerJoin(festival).on(festival.id.eq(stage.festival.id))
                .leftJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
                .where(getDynamicWhere(condition.isPast(), condition.lastStartDate(), condition.lastFestivalId(),
                    condition.currentTime()))
                .orderBy(getDynamicOrderBy(condition.isPast()))
        );
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
            return festival.festivalDuration.startDate.lt(lastStartDate)
                .or(festival.festivalDuration.startDate.eq(lastStartDate)
                    .and(festival.id.gt(lastFestivalId)));
        }
        return festival.festivalDuration.startDate.gt(lastStartDate)
            .or(festival.festivalDuration.startDate.eq(lastStartDate)
                .and(festival.id.gt(lastFestivalId)));
    }

    private BooleanExpression getDefaultWhere(boolean isPast, LocalDate currentTime) {
        if (isPast) {
            return festival.festivalDuration.endDate.lt(currentTime);
        }
        return festival.festivalDuration.endDate.goe(currentTime);
    }

    private OrderSpecifier<LocalDate>[] getDynamicOrderBy(boolean isPast) {
        if (isPast) {
            return new OrderSpecifier[]{festival.festivalDuration.endDate.desc()};
        }
        return new OrderSpecifier[]{festival.festivalDuration.startDate.asc(), festival.id.asc()};
    }
}
