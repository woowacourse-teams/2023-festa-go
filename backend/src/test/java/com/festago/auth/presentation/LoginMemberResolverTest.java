package com.festago.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.auth.domain.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.LoginMember;
import com.festago.exception.UnauthorizedException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LoginMemberResolverTest {

    @InjectMocks
    LoginMemberResolver loginMemberResolver;

    @Mock
    AuthExtractor authExtractor;

    @Mock
    NativeWebRequest request;

    @Test
    void 토큰이_null이면_예외() {
        // given
        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn(null);

        // when & then
        assertThatThrownBy(() -> loginMemberResolver.resolveArgument(null, null, request, null))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage("로그인이 필요한 서비스입니다.");
    }

    @Test
    void Bearer토큰이_아니면_예외() {
        // given
        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn("sampleToken");

        // when & then
        assertThatThrownBy(() -> loginMemberResolver.resolveArgument(null, null, request, null))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage("Bearer 타입의 토큰이 아닙니다.");
    }

    @Test
    void 성공() {
        // given
        String token = "sampleToken";
        Long memberId = 1L;

        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn("Bearer " + token);
        given(authExtractor.extract(token))
            .willReturn(new AuthPayload(memberId));

        // when
        LoginMember expect = loginMemberResolver.resolveArgument(null, null, request, null);

        // then
        assertThat(expect.memberId()).isEqualTo(memberId);
    }
}
