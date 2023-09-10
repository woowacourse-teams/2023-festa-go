package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.auth.infrastructure.FestagoOAuth2Client;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.exception.NotFoundException;
import com.festago.support.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    MemberRepository memberRepository;

    AuthService authService;

    LoginService loginService;

    AuthProvider authProvider;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        OAuth2Clients oAuth2Clients = OAuth2Clients.builder()
            .add(new FestagoOAuth2Client())
            .build();
        memberRepository = mock(MemberRepository.class);
        authProvider = mock(AuthProvider.class);

        authService = new AuthService(loginService, oAuth2Clients, memberRepository, authProvider);
    }

    @Test
    void 로그인() {
        LoginRequest request = new LoginRequest(SocialType.FESTAGO, "1");

        Member member = MemberFixture.member()
            .id(1L)
            .build();

        given(authProvider.provide(any(AuthPayload.class)))
            .willReturn("Bearer token");

        given(loginService.login(any(UserInfo.class)))
            .willReturn(new LoginMemberDto(false, member));

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response)
            .isNotNull();
    }

    @Nested
    class 회원탈퇴 {

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;
            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> authService.deleteMember(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 멤버입니다.");
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            Member member = MemberFixture.member().id(memberId).build();
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> authService.deleteMember(memberId));
        }
    }
}
