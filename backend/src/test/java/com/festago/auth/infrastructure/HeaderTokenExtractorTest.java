package com.festago.auth.infrastructure;

import static com.festago.common.exception.ErrorCode.NOT_BEARER_TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class HeaderTokenExtractorTest {

    HeaderHttpRequestTokenExtractor headerTokenExtractor = new HeaderHttpRequestTokenExtractor();

    @Mock
    HttpServletRequest request;

    @Test
    void 요청에_Authorization_헤더가_없으면_empty() {
        // given
        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn(null);

        // when & then
        assertThat(headerTokenExtractor.extract(request)).isEmpty();
    }

    @Test
    void Bearer_토큰이_아니면_예외() {
        // given
        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn("Bear sampleToken");

        // when & then
        assertThatThrownBy(() -> headerTokenExtractor.extract(request))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessage(NOT_BEARER_TOKEN_TYPE.getMessage());
    }

    @Test
    void Bearer_토큰이_아니면_present() {
        // given
        given(request.getHeader(HttpHeaders.AUTHORIZATION))
            .willReturn("Bearer sampleToken");

        // when & then
        assertThat(headerTokenExtractor.extract(request)).isPresent();
    }
}
