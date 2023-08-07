package com.festago.auth.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.application.integration.ApplicationIntegrationTest;
import com.festago.auth.application.AuthService;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.LoginRequest;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    // TODO #267 이슈 Merge시 MemberService로 이동
    @Test
    void 회원이_탈퇴하고_재가입하면_새로운_계정으로_가입() {
        // given
        LoginRequest request = new LoginRequest(SocialType.FESTAGO, "1");
        authService.login(request);
        Member member = memberRepository.findBySocialIdAndSocialType("1", SocialType.FESTAGO).get();

        // when
        memberRepository.delete(member);
        authService.login(request);

        // then
        assertThat(memberRepository.count()).isEqualTo(1);
    }
}
