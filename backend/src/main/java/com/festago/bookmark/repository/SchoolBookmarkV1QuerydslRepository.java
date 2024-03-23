package com.festago.bookmark.repository;


import static com.festago.bookmark.domain.QBookmark.bookmark;
import static com.festago.school.domain.QSchool.school;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.QSchoolBookmarkTotalV1Response;
import com.festago.bookmark.dto.QSchoolBookmarkV1Response;
import com.festago.bookmark.dto.SchoolBookmarkTotalV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolBookmarkV1QuerydslRepository extends QueryDslRepositorySupport {

    protected SchoolBookmarkV1QuerydslRepository() {
        super(Bookmark.class);
    }

    public List<SchoolBookmarkTotalV1Response> findAllByMemberId(Long memberId) {
        return select(new QSchoolBookmarkTotalV1Response(
                new QSchoolBookmarkV1Response(school.id, school.name, school.logoUrl), bookmark.createdAt))
            .from(bookmark)
            .innerJoin(school).on(school.id.eq(bookmark.resourceId))
            .where(bookmark.bookmarkType.eq(BookmarkType.SCHOOL)
                .and(bookmark.memberId.eq(memberId)))
            .fetch();
    }
}
