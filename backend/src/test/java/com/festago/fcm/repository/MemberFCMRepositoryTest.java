package com.festago.fcm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.fcm.domain.MemberFCM;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.support.MemberFcmFixture;
import com.festago.support.MemberFixture;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
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
    void memberId로_token리스트_조회() {
        // given
        Member member1 = memberRepository.save(MemberFixture.member().build());
        Member member2 = memberRepository.save(MemberFixture.member().build());

        MemberFCM fcm1 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member1.getId()).build());
        MemberFCM fcm2 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member1.getId()).build());
        MemberFCM fcm3 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member2.getId()).build());

        // when
        List<String> actual = memberFCMRepository.findAllTokenByMemberId(member1.getId());

        // then
        assertThat(actual).containsExactlyInAnyOrder(fcm1.getFcmToken(), fcm2.getFcmToken());
    }

    @Nested
    class memberId리스트로_token리스트_조회 {

        @Test
        void 빈리스트면_빈리스트반환() {
            // given
            List<String> actual = memberFCMRepository.findAllTokenByMemberIdIn(Collections.emptyList());

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 성공적으로_조회() {
            // given
            Member member1 = memberRepository.save(MemberFixture.member().build());
            Member member2 = memberRepository.save(MemberFixture.member().build());
            Member member3 = memberRepository.save(MemberFixture.member().build());

            MemberFCM fcm1 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member1.getId()).build());
            MemberFCM fcm2 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member1.getId()).build());
            MemberFCM fcm3 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member2.getId()).build());
            MemberFCM fcm4 = memberFCMRepository.save(MemberFcmFixture.memberFcm().memberId(member3.getId()).build());

            // when
            List<String> actual = memberFCMRepository.findAllTokenByMemberIdIn(
                List.of(member1.getId(), member2.getId()));

            // then
            assertThat(actual).containsExactlyInAnyOrder(fcm1.getFcmToken(), fcm2.getFcmToken(), fcm3.getFcmToken());
        }
    }
}
