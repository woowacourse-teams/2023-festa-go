package com.festago.artist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.artist.dto.command.ArtistUpdateCommand;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.support.fixture.ArtistFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistCommandServiceTest {

    ArtistCommandService artistCommandService;

    ArtistRepository artistRepository;

    @BeforeEach
    void setUp() {
        artistRepository = new MemoryArtistRepository();
        artistCommandService = new ArtistCommandService(artistRepository);
    }

    @Test
    void 아티스트를_저장한다() {
        // given
        ArtistCreateCommand command = new ArtistCreateCommand("윤서연", "https://image.com/image.png",
            "https://image.com/image.png");

        // when
        Long artistId = artistCommandService.save(command);

        // then
        assertThat(artistRepository.findById(artistId)).isPresent();
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        Long artistId = artistRepository.save(ArtistFixture.builder().name("고윤하").build()).getId();
        ArtistUpdateCommand command = new ArtistUpdateCommand("윤하", "https://image.com/image2.png",
            "https://image.com/image2.png");

        // when
        artistCommandService.update(command, artistId);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);

        assertSoftly(softly -> {
            softly.assertThat(actual.getName()).isEqualTo(command.name());
            softly.assertThat(actual.getProfileImage()).isEqualTo(command.profileImageUrl());
            softly.assertThat(actual.getBackgroundImageUrl()).isEqualTo(command.backgroundImageUrl());
        });
    }

    @Test
    void 아티스트를_삭제한다() {
        // given
        Long artistId = artistRepository.save(ArtistFixture.builder().name("고윤하").build()).getId();

        // when
        artistCommandService.delete(artistId);

        // then
        assertThat(artistRepository.findById(artistId)).isEmpty();
    }
}
