package com.festago.auth.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.application.integration.ApplicationIntegrationTest;
import com.festago.auth.application.AuthFacadeService;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.LoginRequest;
import com.festago.support.MemberFixture;
import com.festago.zmember.domain.Member;
import com.festago.zmember.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthFacadeServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthFacadeService authFacadeService;

    @Autowired
    EntityManager entityManager;

    @Test
    void 회원이_탈퇴하고_재가입하면_새로운_계정으로_가입() {
        // given
        LoginRequest request = new LoginRequest(SocialType.FESTAGO, "1");
        authFacadeService.login(request);
        Member member = memberRepository.findBySocialIdAndSocialType("1", SocialType.FESTAGO).get();

        // when
        memberRepository.delete(member);
        authFacadeService.login(request);

        // then
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    @Test
    void 회원탈퇴() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());

        // when
        authFacadeService.deleteMember(member.getId());

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
        authFacadeService.deleteMember(member.getId());

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }
}
