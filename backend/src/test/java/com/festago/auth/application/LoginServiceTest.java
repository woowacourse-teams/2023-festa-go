package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.support.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    MemberRepository memberRepository;


    LoginService loginService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        loginService = new LoginService(memberRepository);
    }

    @Test
    void 신규_회원으로_로그인() {
        // given
        Member member = MemberFixture.member()
            .id(1L)
            .build();
        given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
            .willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class)))
            .willReturn(member);
        UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
            member.getProfileImage());

        // when
        LoginMemberDto response = loginService.login(userInfo);

        // then
        assertThat(response.isNew())
            .isTrue();
    }

    @Test
    void 기존_회원으로_로그인() {
        // given
        Member member = MemberFixture.member()
            .id(1L)
            .build();
        given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
            .willReturn(Optional.of(member));
        UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
            member.getProfileImage());

        // when
        LoginMemberDto response = loginService.login(userInfo);

        // then
        assertThat(response.isNew())
            .isFalse();
    }

}
