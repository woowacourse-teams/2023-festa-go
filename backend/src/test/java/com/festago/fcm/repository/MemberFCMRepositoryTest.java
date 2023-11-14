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
}
