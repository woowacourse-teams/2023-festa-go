package com.festago.school.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolTest {

    @Test
    void 학교의_도메인_길이가_50자를_넘으면_예외() {
        // given
        String domain = "1234567890".repeat(5) + "1";

        // when & then
        assertThatThrownBy(() -> new School(domain, "name"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학교의_이름이_255자를_넘으면_예외() {
        // given
        String name = "1234567890".repeat(25) + "123456";

        // when & then
        assertThatThrownBy(() -> new School("domain", name))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학교의_도메인을_수정할때_255자를_넘으면_예외() {
        // given
        School school = new School("domain", "name");

        // when & then
        String domain = "1234567890".repeat(5) + "1";
        assertThatThrownBy(() -> school.changeDomain(domain))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 학교의_이름을_수정할때_255자를_넘으면_예외() {
        // given
        School school = new School("domain", "name");

        // when & then
        String name = "1234567890".repeat(25) + "123456";
        assertThatThrownBy(() -> school.changeName(name))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 50})
    void 학교를_생성할때_도메인이_50글자_이내_성공(int length) {
        // given
        String domain = "1".repeat(length);

        // when
        School school = new School(domain, "name");

        // then
        assertThat(school.getDomain()).isEqualTo(domain);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 255})
    void 학교를_생성할때_이름이_255글자_이내_성공(int length) {
        // given
        String name = "1".repeat(length);

        // when
        School school = new School("domain", name);

        // then
        assertThat(school.getName()).isEqualTo(name);
    }
}
