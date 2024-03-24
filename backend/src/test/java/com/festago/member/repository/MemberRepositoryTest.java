package com.festago.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.member.domain.Member;
import com.festago.support.RepositoryTest;
import com.festago.support.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void 소셜_아이디와_소셜_타입으로_멤버를_찾는다() {
        // given
        Member member = MemberFixture.builder()
            .build();
        Member expected = memberRepository.save(member);

        // when
        Member actual = memberRepository.findBySocialIdAndSocialType(expected.getSocialId(), expected.getSocialType())
            .get();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 회원_삭제() {
        // given
        Member member = MemberFixture.builder()
            .build();
        Member expected = memberRepository.save(member);

        // when
        memberRepository.delete(expected);
        Member actual = (Member) em.createNativeQuery("select * from member where id = " + expected.getId(),
                Member.class)
            .getSingleResult();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.isDeleted()).isTrue();
            softly.assertThat(actual.getNickname()).isEqualTo("탈퇴한 회원");
            softly.assertThat(actual.getProfileImage()).isEqualTo("");
        });
    }
}
