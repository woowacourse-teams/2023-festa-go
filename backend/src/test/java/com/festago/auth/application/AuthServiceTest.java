package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.dto.LoginMember;
import com.festago.domain.MemberRepository;
import com.festago.exception.UnauthorizedException;
import com.festago.support.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    private static final String BEARER_TOKEN = "Bearer token";
    @Mock
    MemberRepository memberRepository;

    @Mock
    OAuth2Clients oAuth2Clients;

    @Mock
    AuthProvider authProvider;

    @Mock
    AuthExtractor authExtractor;

    @InjectMocks
    AuthService authService;

    @Nested
    class 토큰_헤더로_멤버_로그인 {

        @Test
        void 토큰에_해당하는_유저가_없으면_예외() {
            // given
            Long memberId = 1L;

            given(authExtractor.extract(anyString()))
                .willReturn(new AuthPayload(memberId));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.loginMemberByHeader(BEARER_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("올바르지 않은 로그인 토큰입니다.");
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;

            given(authExtractor.extract(anyString()))
                .willReturn(new AuthPayload(memberId));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(MemberFixture.member().id(memberId).build()));

            // when
            LoginMember loginMember = authService.loginMemberByHeader(BEARER_TOKEN);

            // then
            assertThat(loginMember.memberId()).isEqualTo(memberId);
        }

    }
}
