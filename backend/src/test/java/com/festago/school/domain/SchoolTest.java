package com.festago.school.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.ValidException;
import com.festago.support.fixture.SchoolFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolTest {

    @Nested
    class 생성 {

        @Test
        void 도메인이_50자를_넘으면_예외() {
            // given
            String domain = "1".repeat(51);

            // when & then
            assertThatThrownBy(() -> new School(domain, "테코대학교", "", "", SchoolRegion.서울))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 도메인이_null_또는_공백이면_예외(String domain) {
            // when & then
            assertThatThrownBy(() -> new School(domain, "테코대학교", "", "", SchoolRegion.서울))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 50})
        void 도메인이_50자_이내이면_성공(int length) {
            // given
            String domain = "1".repeat(length);

            // when
            School school = new School(domain, "테코대학교", "", "", SchoolRegion.서울);

            // then
            assertThat(school.getDomain()).isEqualTo(domain);
        }

        @Test
        void 이름이_255자를_넘으면_예외() {
            // given
            String name = "1".repeat(256);

            // when & then
            assertThatThrownBy(() -> new School("teco.ac.kr", name, "", "", SchoolRegion.서울))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 이름이_null_또는_공백이면_예외(String name) {
            // when & then
            assertThatThrownBy(() -> new School("teco.ac.kr", name, "", "", SchoolRegion.서울))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void 이름이_255자_이내이면_성공(int length) {
            // given
            String name = "1".repeat(length);

            // when
            School school = new School("teco.ac.kr", name, "", "", SchoolRegion.서울);

            // then
            assertThat(school.getName()).isEqualTo(name);
        }

        @Test
        void 지역이_null이면_예외() {
            // given
            SchoolRegion region = null;

            // when & then
            assertThatThrownBy(() -> new School("teco.ac.kr", "테코대학교", "", "", region))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void logoUrl이_null_또는_공백이면_기본값이_할당된다(String logoUrl) {
            // when
            School school = new School(1L, "teco.ac.kr", "테코대학교", logoUrl, "https://image.com/backgroundImage.png",
                SchoolRegion.서울);

            // then
            assertThat(school.getLogoUrl()).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void logoUrl이_255자_이내이면_성공(int length) {
            // given
            String logoUrl = "1".repeat(length);

            // when
            School school = new School(1L, "teco.ac.kr", "테코대학교", logoUrl, "https://image.com/backgroundImage.png",
                SchoolRegion.서울);

            // then
            assertThat(school.getLogoUrl()).isEqualTo(logoUrl);
        }

        @Test
        void logoUrl이_255자를_넘으면_예외() {
            // given
            String logoUrl = "1".repeat(256);

            // when & then
            assertThatThrownBy(() -> {
                new School(1L, "teco.ac.kr", "테코대학교", logoUrl, "https://image.com/backgroundImage.png",
                    SchoolRegion.서울);
            }).isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void backgroundImageUrl이_null_또는_공백이면_기본값이_할당된다(String backgroundImageUrl) {
            // when
            School school = new School(1L, "teco.ac.kr", "테코대학교", "https://image.com/logo.png", backgroundImageUrl,
                SchoolRegion.서울);

            // then
            assertThat(school.getBackgroundUrl()).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void backgroundImageUrl이_255자_이내이면_성공(int length) {
            // given
            String backgroundImageUrl = "1".repeat(length);

            // when
            School school = new School(1L, "teco.ac.kr", "테코대학교", "https://image.com/logo.png", backgroundImageUrl,
                SchoolRegion.서울);

            // then
            assertThat(school.getBackgroundUrl()).isEqualTo(backgroundImageUrl);
        }

        @Test
        void backgroundImageUrl이_255자를_넘으면_예외() {
            // given
            String backgroundImageUrl = "1".repeat(256);

            // when & then
            assertThatThrownBy(() -> {
                new School(1L, "teco.ac.kr", "테코대학교", "https://image.com/logo.png", backgroundImageUrl,
                    SchoolRegion.서울);
            }).isInstanceOf(ValidException.class);
        }

        @Test
        void 성공() {
            // given
            Long id = 1L;
            String domain = "teco.ac.kr";
            String name = "테코대학교";
            String logoUrl = "https://image.com/logo.png";
            String backgroundImageUrl = "https://image.com/backgroundImage.png";
            SchoolRegion region = SchoolRegion.서울;

            School school = new School(id, domain, name, logoUrl, backgroundImageUrl, region);

            // when & then
            assertSoftly(softly -> {
                softly.assertThat(school.getId()).isEqualTo(1L);
                softly.assertThat(school.getDomain()).isEqualTo(domain);
                softly.assertThat(school.getName()).isEqualTo(name);
                softly.assertThat(school.getLogoUrl()).isEqualTo(logoUrl);
                softly.assertThat(school.getBackgroundUrl()).isEqualTo(backgroundImageUrl);
                softly.assertThat(school.getRegion()).isEqualTo(region);
            });
        }
    }

    @Nested
    class 수정 {

        School school;

        @BeforeEach
        void setUp() {
            school = new School(1L, "teco.ac.kr", "테코대학교", "https://image.com/logo.png",
                "https://image.com/backgroundImage.png", SchoolRegion.서울);
        }

        @Test
        void 도메인이_51자를_넘으면_예외() {
            // given
            String domain = "1".repeat(51);

            // when & then
            assertThatThrownBy(() -> school.changeDomain(domain))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 도메인이_null_또는_공백이면_예외(String domain) {
            // when & then
            assertThatThrownBy(() -> school.changeDomain(domain))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 50})
        void 도메인이_50자_이내이면_성공(int length) {
            // given
            String domain = "1".repeat(length);

            // when
            school.changeDomain(domain);

            // then
            assertThat(school.getDomain()).isEqualTo(domain);
        }

        @Test
        void 이름이_255자를_넘으면_예외() {
            // when & then
            String name = "1".repeat(256);
            assertThatThrownBy(() -> school.changeName(name))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 이름이_null_또는_공백이면_예외(String name) {
            // when & then
            assertThatThrownBy(() -> school.changeName(name))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void 이름이_255자_이내이면_성공(int length) {
            // given
            String name = "1".repeat(length);

            // when
            school.changeName(name);

            // then
            assertThat(school.getName()).isEqualTo(name);
        }

        @Test
        void 지역이_null이면_예외() {
            // given
            SchoolRegion region = null;

            // when & then
            assertThatThrownBy(() -> school.changeRegion(region))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void 지역이_null이_아니면_성공() {
            // given
            SchoolRegion region = SchoolRegion.대구;

            // when
            school.changeRegion(region);

            // then
            assertThat(school.getRegion()).isEqualTo(region);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void logoUrl이_null_또는_공백이면_기본값이_할당된다(String logoUrl) {
            // when
            school.changeLogoUrl(logoUrl);

            // then
            assertThat(school.getLogoUrl()).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void logoUrl이_255글자_이내이면_성공(int length) {
            String logoUrl = "1".repeat(length);

            // when
            school.changeLogoUrl(logoUrl);

            // then
            assertThat(school.getLogoUrl()).isEqualTo(logoUrl);
        }

        @Test
        void logoUrl이_255자를_넘으면_예외() {
            // given
            String logoUrl = "1".repeat(256);

            // when & then
            assertThatThrownBy(() -> school.changeLogoUrl(logoUrl))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void backgroundImageUrl이_null_또는_공백이면_기본값이_할당된다(String backgroundImageUrl) {
            // when
            school.changeBackgroundImageUrl(backgroundImageUrl);

            // then
            assertThat(school.getBackgroundUrl()).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 255})
        void backgroundImageUrl이_255글자_이내이면_성공(int length) {
            // given
            String backgroundImageUrl = "1".repeat(length);

            // when
            school.changeBackgroundImageUrl(backgroundImageUrl);

            // then
            assertThat(school.getBackgroundUrl()).isEqualTo(backgroundImageUrl);
        }

        @Test
        void backgroundImageUrl이_255자를_넘으면_예외() {
            // given
            String backgroundImageUrl = "1".repeat(256);

            // when & then
            assertThatThrownBy(() -> school.changeBackgroundImageUrl(backgroundImageUrl))
                .isInstanceOf(ValidException.class);
        }
    }

    @Nested
    class getImageUrls {

        @Test
        void 가지고_있는_모든_이미지_URL을_반환한다() {
            // given
            School school = SchoolFixture.builder().build();

            // when
            List<String> imageUrls = school.getImageUrls();

            // then
            assertThat(imageUrls)
                .containsExactlyInAnyOrder(school.getLogoUrl(), school.getBackgroundUrl());
        }
    }
}
