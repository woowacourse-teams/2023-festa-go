package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.artist.dto.command.ArtistUpdateCommand;
import com.festago.artist.repository.ArtistRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistCommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistCommandService artistCommandService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 아티스트를_저장한다() {
        // given
        ArtistCreateCommand command = new ArtistCreateCommand("윤서연", "https://image.com/image.png",
            "https://image.com/image.png");

        // when
        Long artistId = artistCommandService.save(command);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "https://image.com/image1.png")).getId();
        ArtistUpdateCommand command = new ArtistUpdateCommand("윤하", "https://image.com/image2.png",
            "https://image.com/image2.png");

        // when
        artistCommandService.update(command, artistId);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);

        assertSoftly(softly -> {
            softly.assertThat(actual.getName()).isEqualTo(command.name());
            softly.assertThat(actual.getProfileImage()).isEqualTo(command.profileImageUrl());
        });
    }

    @Test
    void 아티스트를_삭제한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "https://image.com/image.png")).getId();

        // when
        artistCommandService.delete(artistId);

        // then
        assertThat(artistRepository.findById(artistId)).isEmpty();
    }
}
