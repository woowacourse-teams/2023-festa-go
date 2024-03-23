package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.member.repository.MemberRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkV1QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolBookmarkV1QueryService schoolBookmarkV1QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Test
    void 특정_회원의_북마크_목록들을_검색한다() {
        // given
        var 회원A_ID = saveMember("socialId_A");
        var 회원B_ID = saveMember("socialId_B");

        var 학교A_ID = saveSchool("A대학교", "a.ac.kr");
        var 학교B_ID = saveSchool("B대학교", "b.ac.kr");
        var 학교C_ID = saveSchool("C대학교", "c.ac.kr");

        saveBookmark(학교A_ID, 회원A_ID);
        saveBookmark(학교B_ID, 회원A_ID);
        saveBookmark(학교C_ID, 회원A_ID);

        saveBookmark(학교A_ID, 회원B_ID);
        saveBookmark(학교B_ID, 회원B_ID);

        // when
        var actual = schoolBookmarkV1QueryService.findAllByMemberId(회원A_ID);

        // then
        assertThat(actual).hasSize(3);
    }

    private Long saveMember(String socialId) {
        return memberRepository.save(MemberFixture.member()
            .socialId(socialId)
            .build()).getId();
    }

    private Long saveSchool(String name, String domain) {
        return schoolRepository.save(SchoolFixture.school()
            .name(name)
            .domain(domain)
            .build()).getId();
    }

    private void saveBookmark(Long schoolId, Long memberId) {
        bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId));
    }
}
