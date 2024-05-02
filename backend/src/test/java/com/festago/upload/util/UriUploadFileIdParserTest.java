package com.festago.upload.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UriUploadFileIdParserTest {

    @ParameterizedTest
    @NullAndEmptySource
    void URI가_null_또는_빈_문자열이면_빈_Optioanl이_반환된다(String uri) {
        // when
        Optional<UUID> actual = UriUploadFileIdParser.parse(uri);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void UUID_형식이라도_확장자가_존재하지_않으면_빈_Optional이_반환된다() {
        // given
        String uri = "https://image.com/" + UUID.randomUUID();

        // when
        Optional<UUID> actual = UriUploadFileIdParser.parse(uri);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void UUID_형식이_아니면_빈_Optional이_반환된다() {
        // given
        String uri = "https://image.com/image.png";

        // when
        Optional<UUID> actual = UriUploadFileIdParser.parse(uri);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void URI의_형식에_스키마가_존재하지_않아도_값이_있는_Optional이_반환된다() {
        // given
        String uri = UUID.randomUUID() + ".png";

        // when
        Optional<UUID> actual = UriUploadFileIdParser.parse(uri);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    void 파일_이름이_UUID_형식이면_값이_있는_Optional이_반환된다() {
        // given
        String uri = "https://image.com/" + UUID.randomUUID() + ".png";

        // when
        Optional<UUID> actual = UriUploadFileIdParser.parse(uri);

        // then
        assertThat(actual).isPresent();
    }
}
