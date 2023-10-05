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
import com.festago.auth.infrastructure.FestagoOAuth2Client;
import com.festago.member.domain.Member;
import com.festago.support.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceTest {

    AuthFacadeService authFacadeService;

    AuthService authService;

    AuthProvider authProvider;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        OAuth2Clients oAuth2Clients = OAuth2Clients.builder()
            .add(new FestagoOAuth2Client())
            .build();
        authProvider = mock(AuthProvider.class);

        authFacadeService = new AuthFacadeService(authService, oAuth2Clients, authProvider);
    }

    @Test
    void 로그인() {
        Member member = MemberFixture.member()
            .id(1L)
            .build();

        given(authProvider.provide(any(AuthPayload.class)))
            .willReturn("Bearer token");

        given(authService.login(any(UserInfo.class)))
            .willReturn(new LoginMemberDto(false, member.getId(), member.getNickname()));

        // when
        LoginResponse response = authFacadeService.login(SocialType.FESTAGO, "1");

        // then
        assertThat(response)
            .isNotNull();
    }
}
