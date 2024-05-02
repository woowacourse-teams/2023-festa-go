package com.festago.upload.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FilenameExtensionParserTest {

    @Test
    void 파일의_확장자가_없으면_빈_문자열이_반환된다() {
        // given
        String filename = "myFile";

        // when
        String extension = FileNameExtensionParser.parse(filename);

        // then
        assertThat(extension).isEmpty();
    }

    @Test
    void 파일의_이름에_점_뒤에_문자열이_없으면_빈_문자열이_반환된다() {
        // given
        String filename = "myFile.";

        // when
        String extension = FileNameExtensionParser.parse(filename);

        // then
        assertThat(extension).isEmpty();
    }

    @Test
    void 파일의_확장자가_있으면_확장자가_반환된다() {
        // given
        String filename = "myFile.png";

        // when
        String extension = FileNameExtensionParser.parse(filename);

        // then
        assertThat(extension).isEqualTo(".png");
    }

    @Test
    void 파일의_이름에_점이_여러개면_마지막_점_기준_확장자가_반환된다() {
        // given
        String filename = "myFile.jpg.png";

        // when
        String extension = FileNameExtensionParser.parse(filename);

        // then
        assertThat(extension).isEqualTo(".png");
    }

    @Test
    void 파일의_마지막에_공백이_있어도_확장자가_반환된다() {
        // given
        String filename = "myFile.png   ";

        // when
        String extension = FileNameExtensionParser.parse(filename);

        // then
        assertThat(extension).isEqualTo(".png");
    }
}
