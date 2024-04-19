package com.festago.bookmark.repository.v1;

import static com.festago.bookmark.domain.QBookmark.bookmark;
import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.school.domain.QSchool.school;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.FestivalBookmarkV1Response;
import com.festago.bookmark.dto.v1.QFestivalBookmarkV1Response;
import com.festago.bookmark.repository.FestivalBookmarkOrder;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.dto.QFestivalV1Response;
import com.festago.festival.dto.QSchoolV1Response;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FestivalBookmarkV1QueryDslRepository extends QueryDslRepositorySupport {

    public FestivalBookmarkV1QueryDslRepository() {
        super(Bookmark.class);
    }

    public List<Long> findBookmarkedFestivalIds(Long memberId) {
        return select(bookmark.resourceId)
            .from(bookmark)
            .where(bookmark.memberId.eq(memberId).and(bookmark.bookmarkType.eq(BookmarkType.FESTIVAL)))
            .fetch();
    }

    public List<FestivalBookmarkV1Response> findBookmarkedFestivals(
        Long memberId,
        List<Long> festivalIds,
        FestivalBookmarkOrder festivalBookmarkOrder
    ) {
        return select(
            new QFestivalBookmarkV1Response(
                new QFestivalV1Response(
                    festival.id,
                    festival.name,
                    festival.festivalDuration.startDate,
                    festival.festivalDuration.endDate,
                    festival.thumbnail,
                    new QSchoolV1Response(
                        school.id,
                        school.name
                    ),
                    festivalQueryInfo.artistInfo
                ),
                bookmark.createdAt
            ))
            .from(festival)
            .innerJoin(school).on(school.id.eq(festival.school.id))
            .innerJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .innerJoin(bookmark).on(bookmark.bookmarkType.eq(BookmarkType.FESTIVAL)
                .and(bookmark.resourceId.eq(festival.id)).and(bookmark.memberId.eq(memberId)))
            .where(festival.id.in(festivalIds))
            .orderBy(dynamicOrder(festivalBookmarkOrder))
            .fetch();
    }

    private OrderSpecifier<?> dynamicOrder(FestivalBookmarkOrder festivalBookmarkOrder) {
        return switch (festivalBookmarkOrder) {
            case BOOKMARK -> bookmark.id.desc();
            case FESTIVAL -> festival.festivalDuration.startDate.asc();
        };
    }
}
