package com.festago.school.domain;

import static com.festago.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.festago.common.exception.ErrorCode.INVALID_SCHOOL_DOMAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolTest {

    @Nested
    class domain_검증 {

        @ValueSource(strings = {"", "a", ".a", "a.", "a..a"})
        @ParameterizedTest
        void 올바르지_않은_domain이면_예외(String domain) {
            // when & then
            assertThatThrownBy(() -> new School(domain, "테코대학교"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_SCHOOL_DOMAIN.getMessage());
        }

        @ValueSource(strings = {"festa.com", "festa.go.com"})
        @ParameterizedTest
        void 올바른_domain이면_성공(String domain) {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> new School(domain, "테코대학교"));
        }
    }

    @Nested
    class null_체크 {

        @Test
        void domain이_null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new School(null, "테코대학교"))
                .isInstanceOf(InternalServerException.class)
                .hasMessage(INTERNAL_SERVER_ERROR.getMessage());
        }

        @Test
        void name이_null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new School("festa.com", null))
                .isInstanceOf(InternalServerException.class)
                .hasMessage(INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Nested
    class 길이_검증 {

        @Test
        void domain이_50자보다_길면_예외() {
            // given
            String domain = "a".repeat(49) + ".b";

            // when & then
            assertThatThrownBy(() -> new School(domain, "테코대학교"))
                .isInstanceOf(InternalServerException.class)
                .hasMessage(INTERNAL_SERVER_ERROR.getMessage());
        }

        @Test
        void name이_255자보다_길면_예외() {
            // given
            String name = "a".repeat(256);

            // when & then
            assertThatThrownBy(() -> new School("festa.com", name))
                .isInstanceOf(InternalServerException.class)
                .hasMessage(INTERNAL_SERVER_ERROR.getMessage());
        }
    }


    @Test
    void 약어_조회() {
        // given
        School school = new School("festa.ac.kr", "테코대학교");

        // when
        String abbreviation = school.findAbbreviation();

        // then
        assertThat(abbreviation).isEqualTo("festa");
    }
}
