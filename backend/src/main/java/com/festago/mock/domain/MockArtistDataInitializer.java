package com.festago.mock.domain;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.mock.MockArtist;
import com.festago.mock.repository.ForMockArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Order(0)
@Component
@Transactional
@RequiredArgsConstructor
public class MockArtistDataInitializer implements MockDataInitializer {

    private final ForMockArtistRepository artistRepository;
    private final ArtistCommandService artistCommandService;

    @Override
    public boolean canInitialize() {
        return artistRepository.count() == 0;
    }

    @Override
    public void initialize() {
        for (MockArtist artist : MockArtist.values()) {
            artistCommandService.save(new ArtistCreateCommand(
                    artist.name(),
                    artist.getProfileImage(),
                    artist.getBackgroundImageUrl()
                )
            );
        }
    }
}
