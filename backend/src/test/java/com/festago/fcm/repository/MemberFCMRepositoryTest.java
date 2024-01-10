package com.festago.fcm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.SocialType;
import com.festago.fcm.domain.MemberFCM;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MemberFCMRepositoryTest {

    @Autowired
    MemberFCMRepository memberFCMRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void member의_모든_fcmToken_조회() {
        // given
        Member member = memberRepository.save(new Member("socialId", SocialType.FESTAGO, "nickname", "image.jpg"));
        Long memberId = member.getId();
        MemberFCM expect1 = memberFCMRepository.save(new MemberFCM(memberId, "fcmToken"));
        MemberFCM expect2 = memberFCMRepository.save(new MemberFCM(memberId, "fcmToken2"));

        // when
        List<MemberFCM> actual = memberFCMRepository.findAllByMemberId(memberId);

        // then
        assertThat(actual).containsExactly(expect1, expect2);
    }
}
