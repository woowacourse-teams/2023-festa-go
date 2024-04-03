package com.festago.bookmark.repository.v1;

import static com.festago.bookmark.domain.QBookmark.bookmark;
import static com.festago.school.domain.QSchool.school;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.QSchoolBookmarkInfoV1Response;
import com.festago.bookmark.dto.v1.QSchoolBookmarkV1Response;
import com.festago.bookmark.dto.v1.SchoolBookmarkV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolBookmarkV1QuerydslRepository extends QueryDslRepositorySupport {

    protected SchoolBookmarkV1QuerydslRepository() {
        super(Bookmark.class);
    }

    public List<SchoolBookmarkV1Response> findAllByMemberId(Long memberId) {
        return select(new QSchoolBookmarkV1Response(
            new QSchoolBookmarkInfoV1Response(
                school.id,
                school.name,
                school.logoUrl
            ),
            bookmark.createdAt
        ))
            .from(bookmark)
            .innerJoin(school).on(school.id.eq(bookmark.resourceId)
                .and(bookmark.memberId.eq(memberId))
                .and(bookmark.bookmarkType.eq(BookmarkType.SCHOOL)))
            .fetch();
    }
}
