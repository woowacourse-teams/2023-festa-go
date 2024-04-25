package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.LoginResponse;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.infrastructure.FestagoOAuth2Client;
import com.festago.member.domain.Member;
import com.festago.support.fixture.MemberFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Deprecated(forRemoval = true)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceTest {

    AuthFacadeService authFacadeService;

    AuthService authService;

    AuthTokenProvider authTokenProvider;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        OAuth2Clients oAuth2Clients = OAuth2Clients.builder()
            .add(new FestagoOAuth2Client())
            .build();
        authTokenProvider = mock(AuthTokenProvider.class);

        authFacadeService = new AuthFacadeService(authService, oAuth2Clients, authTokenProvider);
    }

    @Test
    void 로그인() {
        Member member = MemberFixture.builder()
            .id(1L)
            .build();

        given(authTokenProvider.provide(any(AuthPayload.class)))
            .willReturn(new TokenResponse("Bearer token", LocalDateTime.now().plusWeeks(1)));

        given(authService.login(any(UserInfo.class)))
            .willReturn(new LoginMemberDto(false, member.getId(), member.getNickname()));

        // when
        LoginResponse response = authFacadeService.login(SocialType.FESTAGO, "1");

        // then
        assertThat(response)
            .isNotNull();
    }
}
