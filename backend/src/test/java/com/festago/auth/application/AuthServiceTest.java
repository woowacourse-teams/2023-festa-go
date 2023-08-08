package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.OAuth2Clients;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import com.festago.auth.infrastructure.FestagoOAuth2Client;
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
class AuthServiceTest {

    MemberRepository memberRepository;

    AuthProvider authProvider;

    AuthService authService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        OAuth2Clients oAuth2Clients = OAuth2Clients.builder()
            .add(new FestagoOAuth2Client())
            .build();
        authProvider = mock(AuthProvider.class);
        authService = new AuthService(memberRepository, oAuth2Clients, authProvider);
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
        given(authProvider.provide(any(AuthPayload.class)))
            .willReturn("Bearer token");
        LoginRequest request = new LoginRequest(SocialType.FESTAGO, "1");

        // when
        LoginResponse response = authService.login(request);

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
        given(authProvider.provide(any(AuthPayload.class)))
            .willReturn("Bearer token");
        LoginRequest request = new LoginRequest(SocialType.FESTAGO, "1");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response.isNew())
            .isFalse();
    }
}
