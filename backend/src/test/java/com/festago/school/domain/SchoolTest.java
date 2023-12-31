package com.festago.school.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolTest {

    @Test
    void 학교의_도메인_길이가_50자를_넘으면_예외() {
        // given
        String domain = "1".repeat(51);

        // when & then
        assertThatThrownBy(() -> new School(domain, "테코대학교"))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void 학교의_도메인이_null_또는_공백이면_예외(String domain) {
        // when & then
        assertThatThrownBy(() -> new School(domain, "테코대학교"))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void 학교의_이름이_255자를_넘으면_예외() {
        // given
        String name = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new School("teco.ac.kr", name))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void 학교의_이름이_null_또는_공백이면_예외(String name) {
        // when & then
        assertThatThrownBy(() -> new School("teco.ac.kr", name))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void 학교의_도메인을_수정할때_255자를_넘으면_예외() {
        // given
        School school = new School("teco.ac.kr", "테코대학교");

        // when & then
        String domain = "1".repeat(256);
        assertThatThrownBy(() -> school.changeDomain(domain))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void 학교의_도메인을_수정할때_null_또는_공백이면_예외(String domain) {
        // given
        School school = new School("teco.ac.kr", "테코대학교");

        // when & then
        assertThatThrownBy(() -> school.changeDomain(domain))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void 학교의_이름을_수정할때_255자를_넘으면_예외() {
        // given
        School school = new School("teco.ac.kr", "테코대학교");

        // when & then
        String name = "1".repeat(256);
        assertThatThrownBy(() -> school.changeName(name))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void 학교의_이름을_수정할때_null_또는_공백이면_예외(String name) {
        // given
        School school = new School("teco.ac.kr", "테코대학교");

        // when & then
        assertThatThrownBy(() -> school.changeName(name))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void 학교_생성_성공() {
        // given
        School school = new School("teco.ac.kr", "테코대학교");

        // when & then
        assertThat(school.getName()).isEqualTo("테코대학교");
        assertThat(school.getDomain()).isEqualTo("teco.ac.kr");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 50})
    void 학교를_생성할때_도메인이_50글자_이내_성공(int length) {
        // given
        String domain = "1".repeat(length);

        // when
        School school = new School(domain, "테코대학교");

        // then
        assertThat(school.getDomain()).isEqualTo(domain);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 255})
    void 학교를_생성할때_이름이_255글자_이내_성공(int length) {
        // given
        String name = "1".repeat(length);

        // when
        School school = new School("teco.ac.kr", name);

        // then
        assertThat(school.getName()).isEqualTo(name);
    }
}
