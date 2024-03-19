package com.festago.common.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UriPatternMatcherTest {

    UriPatternMatcher uriPatternMatcher;

    @BeforeEach
    void setUp() {
        uriPatternMatcher = new UriPatternMatcher();
    }

    @Test
    void 경로가_패턴에_매칭되면_참이다() {
        // given
        uriPatternMatcher.addPattern(Set.of("POST"), Set.of("/api/v1/schools"));

        // when & then
        assertThat(uriPatternMatcher.match("POST", "/api/v1/schools")).isTrue();
    }

    @Test
    void 경로가_패턴에_매칭되지_않으면_거짓이다() {
        // given
        uriPatternMatcher.addPattern(Set.of("POST"), Set.of("/api/v1/schools"));

        // when & then
        assertThat(uriPatternMatcher.match("POST", "/api/v1/festivals")).isFalse();
    }

    @Test
    void 경로가_매칭되어도_HttpMethod가_매칭되지_않으면_거짓이다() {
        // given
        uriPatternMatcher.addPattern(Set.of("GET"), Set.of("/api/v1/schools"));

        // when & then
        assertThat(uriPatternMatcher.match("POST", "/api/v1/schools")).isFalse();
    }

    @Test
    void PathVariable_경로가_패턴에_매칭되어야_한다() {
        // given
        uriPatternMatcher.addPattern(Set.of("GET"), Set.of("/api/v1/schools/{schoolId}"));

        // when & then
        assertThat(uriPatternMatcher.match("GET", "/api/v1/schools/1")).isTrue();
    }
}
