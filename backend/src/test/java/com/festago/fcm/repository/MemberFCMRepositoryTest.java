package com.festago.fcm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.SocialType;
import com.festago.fcm.domain.MemberFCM;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.support.MemberFixture;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberFCMRepositoryTest {

    @Autowired
    MemberFCMRepository memberFCMRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void member_의_MemberFCM_을_찾을_수_있다() {
        // given
        Member member = memberRepository.save(new Member("socialId", SocialType.FESTAGO, "nickname", "image.jpg"));
        Long memberId = member.getId();
        MemberFCM expect = memberFCMRepository.save(new MemberFCM(memberId, "fcmToken"));

        // when
        List<MemberFCM> actual = memberFCMRepository.findByMemberId(memberId);

        // then
        assertThat(actual).contains(expect);
    }

    @Test
    void memberId리스트로_MemberFcm을_찾을_수_있다() {
        // given
        Member member1 = memberRepository.save(MemberFixture.member().build());
        Member member2 = memberRepository.save(MemberFixture.member().build());
        Member member3 = memberRepository.save(MemberFixture.member().build());
        MemberFCM token1 = memberFCMRepository.save(new MemberFCM(member1.getId(), "token1"));
        MemberFCM token2 = memberFCMRepository.save(new MemberFCM(member1.getId(), "token2"));
        MemberFCM token3 = memberFCMRepository.save(new MemberFCM(member2.getId(), "token3"));
        MemberFCM token4 = memberFCMRepository.save(new MemberFCM(member3.getId(), "token4"));

        // when
        List<MemberFCM> actual = memberFCMRepository.findAllByMemberIdIn(
            List.of(member1.getId(), member2.getId()));

        // then
        assertThat(actual).containsExactlyInAnyOrder(token1, token2, token3);
    }
}
