package com.festago.mock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.artist.domain.Artist;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockArtistsGeneratorTest {

    MockArtistsGenerator mockArtistsGenerator = new MockArtistsGenerator();

    @Test
    void MockArtist_목록으로_아티스트를_생성한다() {
        // when
        List<Artist> actual = mockArtistsGenerator.generate();

        // then
        List<String> expect = Arrays.stream(MockArtist.values())
            .map(Enum::name)
            .toList();
        assertThat(actual)
            .map(Artist::getName)
            .containsExactlyInAnyOrderElementsOf(expect);
    }
}
