package com.festago.bookmark.repository;


import static com.festago.bookmark.domain.QBookmark.bookmark;
import static com.festago.school.domain.QSchool.school;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.QSchoolBookmarkV1Response;
import com.festago.bookmark.dto.v1.SchoolBookmarkV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.dto.v1.QSchoolSearchV1Response;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolBookmarkV1QuerydslRepository extends QueryDslRepositorySupport {

    protected SchoolBookmarkV1QuerydslRepository() {
        super(Bookmark.class);
    }

    public List<SchoolBookmarkV1Response> findAllByMemberId(Long memberId) {
        return select(new QSchoolBookmarkV1Response(
                new QSchoolSearchV1Response(school.id, school.name, school.logoUrl), bookmark.createdAt))
            .from(bookmark)
            .innerJoin(school).on(school.id.eq(bookmark.resourceId))
            .where(bookmark.bookmarkType.eq(BookmarkType.SCHOOL)
                .and(bookmark.memberId.eq(memberId)))
            .fetch();
    }
}
