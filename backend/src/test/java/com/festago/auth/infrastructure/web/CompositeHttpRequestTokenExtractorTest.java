package com.festago.auth.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CompositeHttpRequestTokenExtractorTest {

    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void HttpRequestTokenExtractors_모두_빈_옵셔널을_반환하면_빈_옵셔널을_반환한다() {
        // given
        CompositeHttpRequestTokenExtractor compositeHttpRequestTokenExtractor = new CompositeHttpRequestTokenExtractor(
            List.of(req -> Optional.empty(), req -> Optional.empty())
        );

        // when
        Optional<String> actual = compositeHttpRequestTokenExtractor.extract(request);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void HttpRequestTokenExtractors_중_하나라도_빈_옵셔널이_아닌_값을_반환하면_해당_값을_반환한다() {
        // given
        CompositeHttpRequestTokenExtractor compositeHttpRequestTokenExtractor = new CompositeHttpRequestTokenExtractor(
            List.of(req -> Optional.empty(), req -> Optional.of("present"))
        );

        // when
        Optional<String> actual = compositeHttpRequestTokenExtractor.extract(request);

        // then
        assertThat(actual).contains("present");
    }
}
