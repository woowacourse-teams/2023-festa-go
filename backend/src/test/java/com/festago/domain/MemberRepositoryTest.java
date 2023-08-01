package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.auth.domain.SocialType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 소셜_아이디와_소셜_타입으로_멤버를_찾는다() {
        // given
        Member expected = memberRepository.save(
            new Member("socialId", SocialType.KAKAO, "nickname", "www.iamgeurl.com"));

        // when
        Member actual = memberRepository.findBySocialIdAndSocialType(expected.getSocialId(), expected.getSocialType())
            .get();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
