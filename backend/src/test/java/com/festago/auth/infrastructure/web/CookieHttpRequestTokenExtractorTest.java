package com.festago.auth.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CookieHttpRequestTokenExtractorTest {

    CookieHttpRequestTokenExtractor cookieTokenExtractor = new CookieHttpRequestTokenExtractor();

    @Mock
    HttpServletRequest request;

    @Test
    void 요청에_쿠키가_없으면_empty() {
        // given
        given(request.getCookies())
            .willReturn(null);

        // when & then
        assertThat(cookieTokenExtractor.extract(request)).isEmpty();
    }

    @Test
    void 쿠키에_token_헤더가_없으면_empty() {
        // given
        given(request.getCookies())
            .willReturn(new Cookie[]{new Cookie("tokken", "token")});

        // when
        assertThat(cookieTokenExtractor.extract(request)).isEmpty();
    }

    @Test
    void 쿠키에_token_헤더가_있으면_present() {
        // given
        given(request.getCookies())
            .willReturn(new Cookie[]{new Cookie("token", "token")});

        // when
        assertThat(cookieTokenExtractor.extract(request)).isPresent();
    }
}
