package com.festago.fcm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.SocialType;
import com.festago.fcm.domain.MemberFCM;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
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
        MemberFCM expect = memberFCMRepository.save(new MemberFCM(member, "fcmToken"));

        // when
        List<MemberFCM> actual = memberFCMRepository.findByMemberId(member.getId());

        // then
        assertThat(actual).contains(expect);
    }
}
