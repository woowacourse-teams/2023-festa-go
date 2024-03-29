package com.festago.bookmark.repository.v1;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.bookmark.domain.QBookmark.bookmark;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.ArtistBookmarkV1Response;
import com.festago.bookmark.dto.v1.QArtistBookmarkInfoV1Response;
import com.festago.bookmark.dto.v1.QArtistBookmarkV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistBookmarkV1QueryDslRepository extends QueryDslRepositorySupport {

    protected ArtistBookmarkV1QueryDslRepository() {
        super(Bookmark.class);
    }

    public List<ArtistBookmarkV1Response> findByMemberId(Long memberId) {
        return select(
            new QArtistBookmarkV1Response(
                new QArtistBookmarkInfoV1Response(
                    artist.name,
                    artist.profileImage
                ),
                bookmark.createdAt))
            .from(bookmark)
            .innerJoin(artist).on(
                bookmark.bookmarkType.eq(BookmarkType.ARTIST)
                    .and(bookmark.memberId.eq(memberId))
                    .and(bookmark.resourceId.eq(artist.id)))
            .fetch();
    }
}
