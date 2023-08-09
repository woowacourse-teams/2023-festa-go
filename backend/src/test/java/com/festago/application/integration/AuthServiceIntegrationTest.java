package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.auth.application.AuthService;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.support.MemberFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class AuthServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    @Autowired
    EntityManager entityManager;

    @Test
    void 회원탈퇴() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());

        // when
        authService.deleteMember(member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        String sql = "SELECT * FROM member WHERE id = :memberId AND deleted_at IS NOT NULL";
        List<Member> deletedMembers = entityManager.createNativeQuery(sql, Member.class)
            .setParameter("memberId", member.getId())
            .getResultList();

        assertSoftly(softly -> {
            softly.assertThat(deletedMembers.size()).isOne();
            Member actual = deletedMembers.get(0);
            softly.assertThat(actual.getId()).isEqualTo(member.getId());
            softly.assertThat(actual.getNickname()).isEqualTo("탈퇴한 회원");
            softly.assertThat(actual.getProfileImage()).isBlank();
        });
    }


    @Test
    void 탈퇴한_회원은_조회되지않는다() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());

        // when
        authService.deleteMember(member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }
}
