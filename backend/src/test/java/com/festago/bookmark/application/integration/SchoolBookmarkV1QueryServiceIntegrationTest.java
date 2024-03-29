package com.festago.bookmark.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.bookmark.application.SchoolBookmarkV1QueryService;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.SchoolBookmarkV1Response;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.member.repository.MemberRepository;
import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.BookmarkFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.SchoolFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

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

        var 학교A_ID = saveSchool("A대학교", "a.ac.kr", "https://www.festago.com/A.png");
        var 학교B_ID = saveSchool("B대학교", "b.ac.kr", "https://www.festago.com/B.png");

        saveBookmark(학교A_ID, 회원A_ID);
        saveBookmark(학교B_ID, 회원A_ID);

        saveBookmark(학교A_ID, 회원B_ID);
        saveBookmark(학교B_ID, 회원B_ID);

        // when
        var actual = schoolBookmarkV1QueryService.findAllByMemberId(회원A_ID);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(2);
            softly.assertThat(actual).allSatisfy(it -> assertThat(it).hasNoNullFieldsOrProperties());
            softly.assertThat(actual).map(SchoolBookmarkV1Response::school)
                .containsExactly(
                    new SchoolSearchV1Response(학교A_ID, "A대학교", "https://www.festago.com/A.png"),
                    new SchoolSearchV1Response(학교B_ID, "B대학교", "https://www.festago.com/B.png")
                );
        });
    }

    private Long saveMember(String socialId) {
        return memberRepository.save(MemberFixture.builder()
            .socialId(socialId)
            .build()).getId();
    }

    private Long saveSchool(String name, String domain, String logoUrl) {
        return schoolRepository.save(SchoolFixture.builder()
            .name(name)
            .domain(domain)
            .logoUrl(logoUrl)
            .build()).getId();
    }

    private void saveBookmark(Long schoolId, Long memberId) {
        bookmarkRepository.save(
            BookmarkFixture.builder()
                .bookmarkType(BookmarkType.SCHOOL)
                .resourceId(schoolId)
                .memberId(memberId)
                .build()
        );
    }
}
